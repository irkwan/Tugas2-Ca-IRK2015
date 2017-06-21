// File : bignumber.cpp
// Nama : Dewita Sonya Tarabunga
// NIM : 13515021

#include <algorithm>
#include <vector>
#include <iostream>
#include <cstdlib>
#include <ctime>
#include "bignumber.h"
using namespace std;

/* Constructor */
BigNumber::BigNumber() {
	num.clear();
	num.push_back(0);
	negative = false;
}

BigNumber::BigNumber(string number) {
	num.clear();
	if (number.size() > 0) {
		negative = (number[0] == '-');
		int i = 0;
		if (negative) {
			++i;
		}
		while (i < number.size() && number[i] == '0') {
			++i;
		}
		for (; i < number.size(); ++i) {
			num.push_back(number[i] - '0');
		}
		if (num.empty()) {
			negative = false;
			num.push_back(0);
		}
	}
}

BigNumber::BigNumber(const BigNumber& number) {
	if (this != &number) {
		num.clear();
		for (int i = 0; i < number.size(); ++i) {
			num.push_back(number.num[i]);
		}
		negative = number.negative;
	}
}

/* operator= */
BigNumber& BigNumber::operator=(const BigNumber& number) {
	if (this != &number) {
		num.resize(number.size());
		for (int i = 0; i < number.size(); ++i) {
			num[i] = number.num[i];
		}
		negative = number.negative;	
	}
	return *this;
}

/* Input Output */
ostream& operator<< (ostream& os, const BigNumber& number) {
	if (number.negative) {
		os << '-';
	}
	for (int i = 0; i < number.size(); ++i) {
		os << number.num[i];
	}
	return os;
}

void operator>> (istream& is, BigNumber& number) {
	string s;
	is >> s;
	number.num.clear();
	int i = 0;
	if (s[0] == '-') {
		number.negative = true;
		++i;
	} else {
		number.negative = false;
	}
	while (i < s.size() && s[i] == '0') {
		++i;
	}
	for (; i < s.size(); ++i) {
		number.num.push_back(s[i] - '0');
	}
	if (number.num.empty()) {
		number.num.push_back(0);
		number.negative = false;
	}
}

/* Binary Arithmetic Operators */
BigNumber& BigNumber::operator+=(const BigNumber& number) {
	int carry = 0;
	if (negative == number.negative) {
		int i = size(), j = number.size(), maks = i;
		if (j > i) {
			num.resize(j + 1);
			maks = j;
		} else {
			num.push_back(0);
		}
		--i; --j;
		while (i >= 0 && j >= 0) {
			num[maks] = num[i] + number.num[j] + carry;
			if (num[maks] > 9) {
				num[maks] -= 10;
				carry = 1;
			} else {
				carry = 0;
			}
			--i; --j; --maks;
		}
		while (i >= 0) {
			num[maks] = num[i] + carry;
			if (num[maks] > 9) {
				num[maks] -= 10;
				carry = 1;
			} else {
				carry = 0;
			}
			--i; --maks;
		}
		while (j >= 0) {
			num[maks] = number.num[j] + carry;
			if (num[maks] > 9) {
				num[maks] -= 10;
				carry = 1;
			} else {
				carry = 0;
			}
			--j; --maks;
		}
		if (carry == 1) {
			num[0] = 1;
		} else {
			num.erase(num.begin());
		}
	} else {
		operator-=(number.Negate());
	}
	return *this;
}

BigNumber& BigNumber::operator-=(const BigNumber& number) {
	int carry = 0;
	if (negative == number.negative) {
		if (!negative) {
			if (operator>=(number)) {
				int i = size() - 1, j = number.size() - 1;
				while (i >= 0 && j >= 0) {
					num[i] -= (number.num[j] + carry);
					if (num[i] < 0) {
						num[i] += 10;
						carry = 1;
					} else {
						carry = 0;
					}
					--i; --j;
				}
				while (i >= 0) {
					num[i] -= carry;
					if (num[i] == -1) {
						num[i] = 9;
					} else {
						i = 0;
					}
					--i;
				}
				while (j >= 0) {
					num[j] = number.num[j] + carry;
					if (num[j] == -1) {
						num[j] = 9;
					} else {
						for (int k = 0; k < j; ++k) {
							num[k] = number.num[k];
						}
						j = 0;
					}
					--j;
				}
				while (!num.empty() && num[0] == 0) {
					num.erase(num.begin());
				}
				if (num.empty()) {
					negative = false;
					num.push_back(0);
				}
			} else {
				BigNumber temp(number);
				temp.operator-=(*this);
				temp.negative = true;
				operator=(temp);
			}
		} else {
			negative = false;
			operator-=(number.Negate());
			if (num[0] == 0) {
				negative = false;
			} else {
				negative = !negative;
			}
		}
	} else {
		operator+=(number.Negate());
	}
	return *this;
}

BigNumber& BigNumber::operator*=(BigNumber number) {
	bool neg = negative != number.negative;
	negative = false;
	number.negative = false;
	int size1 = size(), size2 = number.size(), sze;
	if (size1 > size2) {
		number >> (size1 - size2);
		sze = size1;
	} else {
		operator>>(size2 - size1);
		sze = size2;
	}
	if (sze == 1) {
		int result = num[0] * number.num[0];
		num[0] = result;
		if (result >= 10) {
			num.push_back(result % 10);
			num[0] /= 10;
		}
	} else { //Karatsuba
		int m = (sze + 1) / 2;
		BigNumber x = Truncate(m), y = number.Truncate(m), z = *this + x, z0 = x * y;
		operator*=(number);
		BigNumber z1 = (z * (y + number)) - z0 - *this;
		operator<<(2*m);
		operator+=(z1 << m);
		operator+=(z0);
	}
	negative = neg;
	if (num[0] == 0) {
		negative = false;
	}
	return *this;
}

BigNumber& BigNumber::operator%=(const BigNumber& number) {
	BigNumber temp(*this), dig;
	num.clear();
	num.push_back(0);
	negative = false;
	for (int i = 0; i < temp.size(); ++i) {
		operator<<(1);
		dig.num[0] = temp.num[i];
		operator+=(dig);
		while (operator>=(number)) {
			operator-=(number);
		}
	}
	return *this;
}

BigNumber& BigNumber::ModPow(BigNumber pow, const BigNumber& mod) {
	if (mod.size() == 1 && mod.num[0] == 1) { // mod == 1 
		num.clear();
		negative = false;
		num.push_back(0);
	} else {
		operator%=(mod);
		BigNumber base(*this);
		pow--;
		while (pow.num[0] != 0) {
			operator*=(base);
			operator%=(mod);
			pow--;
		}
	}
	return *this;
}

/* Unary Arithmetic Operator */
BigNumber& BigNumber::operator++() {
	if (negative) {
		negative = false;
		operator--();
		if (num[0] != 0) {
			negative = true;
		}
	} else {
		bool done = false;
		int i = num.size()-1;
		while (!done && i >= 0) {
			num[i] = (num[i] + 1) % 10;
			if (num[i] > 0) {
				done = true;
			} else {
				--i;
			}
		}
		if (!done) {
			num.push_back(0);
			num[0] = 1;
		}
	}
	return *this;
}

BigNumber& BigNumber::operator--() {
	if (negative) {
		negative = false;
		operator++();
		negative = !negative;
	} else {
		if (num[0] == 0) {
			negative = true;
			num[0] = 1;
		} else {
			bool done = false;
			int i = num.size()-1;
			while (!done) {
				num[i] = (num[i] + 9) % 10;
				if (num[i] < 9) {
					done = true;
				} else {
					--i;
				}
			}
			if (num[0] == 0 && num.size() > 1) {
				num.erase(num.begin());
			}
		}
	}
	return *this;
}

/* Comparison Operator */
bool BigNumber::operator==(const BigNumber& number) const {
	if (negative != number.negative) {
		return false;
	} else if (size() != number.size()) {
		return false;
	} else {
		bool same = true;
		int i = 0, sze = size();
		while (same && i < sze) {
			if (num[i] != number.num[i]) {
				same = false;
			} else {
				++i;
			}
		}
		return same;
	}
}

bool BigNumber::operator<(const BigNumber& number) const {
	if (negative == number.negative) {
		bool less, same = true;
		if (size() == number.size()) {
			int i = 0, sze = size();
			while (same && i < sze) {
				if (num[i] != number.num[i]) {
					same = false;
					less = num[i] < number.num[i];
				} else {
					++i;
				}
			}
		} else {
			same = false;
			less = size() < number.size();
		}
		return (less^negative && !same);
	} else {
		return (negative > number.negative);
	}
}

/* Shift Operator */
BigNumber& BigNumber::operator<<(int len) {
	if (num[0] != 0) {
		if (len >= 0) {
			for (int i = 0; i < len; ++i) {
				num.push_back(0);
			}
		} else {
			operator>>(-len);
		}
	}
	return *this;
}

BigNumber& BigNumber::operator>>(int len) {
	if (len >= 0) {
		for (int i = 0; i < len; ++i) {
			num.insert(num.begin(),0);
		}
	} else {
		operator<<(-len);
	}
	return *this;
}

/* Random */
BigNumber BigNumber::Random() const {
	BigNumber temp;
	srand((unsigned)time(0));
	temp.num[0] = rand() % num[0];
	for (int i = 1; i < size(); ++i) {
		temp.num.push_back(rand() % 10);
	}
	while (!temp.num.empty() && temp.num[0] == 0) {
		temp.num.erase(temp.num.begin());
	}
	if (temp.num.empty() || (temp.num.size() == 1 && temp.num[0] == 1)) {
		temp.num.resize(1);
		temp.num[0] = 2;
	}
	temp.negative = negative;
	return temp;
}

BigNumber BigNumber::GenerateRandomPrime(int digit) { //Fermat Primality Testing
	BigNumber temp, cek, one("1");
	bool prime = false, test;
	srand((unsigned)time(0) + digit);
	while (!prime) {
		temp.num.clear();
		temp.num.push_back(rand() % 9 + 1);
		for (int i = 1; i < digit - 1; ++i) {
			temp.num.push_back(rand() % 10);
		}
		temp.num.push_back(2 * (rand() % 5) + 1);
		int i = 0;
		test = true;
		while (test && i < 5) {
			cek = temp.Random();
			cek.ModPow(temp - one, temp);
			if (cek.size() != 1 || cek.num[0] != 1) {
				test = false;
			}
			++i;
		}
		if (test) {
			prime = true;
		}
	}
	return temp;
}


/* Other */
BigNumber BigNumber::Negate() const {
	BigNumber temp(*this);
	if (num[0] != 0) {
		temp.negative = !negative;	
	}
	return temp;
}

BigNumber BigNumber::Truncate(int len) {
	BigNumber temp;
	if (len > 0 && len < size()) {
		temp.num.pop_back();
		for (int i = 0; i < len; ++i) {
			temp.num.insert(temp.num.begin(), num[num.size()-1]);
			num.pop_back();
		}
	}
	while (!num.empty() && num[0] == 0) {
		num.erase(num.begin());
	}
	if (num.empty()) {
		negative = false;
		num.push_back(0);
	}
	while (!temp.num.empty() && temp.num[0] == 0) {
		temp.num.erase(temp.num.begin());
	}
	if (temp.num.empty()) {
		temp.negative = false;
		temp.num.push_back(0);
	}
	temp.negative = negative;
	return temp;
}