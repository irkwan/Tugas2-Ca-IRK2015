#include "BigInteger.h"
#include <string>
#include <vector>
#include <iostream>
#include <algorithm>
#include <deque>

using namespace std;

BigInteger::BigInteger() {
  sign = true;
}

BigInteger::BigInteger(int num) {
  if (num >= 0) {
    sign = true;
  } else {
    sign = false;
    num *= (-1);
  }
  do {
    number.push_back((char)(num % 10));
    num /= 10;
  } while (num != 0);
}

BigInteger::BigInteger(string& num) {
  sign = true;
  for (string::reverse_iterator idx = num.rbegin(); idx < num.rend(); ++idx) {
    char c = (*idx);
    if (idx == num.rend()-1) {
      if (c == '+') {
        break;
      }
      if (c == '-') {
        sign = false;
        break;
      }
    }
  }
  trimZero();
}

BigInteger::BigInteger(const BigInteger& big) {
  number = big.number;
  sign = big.sign;
}

BigInteger BigInteger::operator=(const BigInteger& big){
  if (this != &big) {
    number = big.number;
    sign = big.sign;
  }
  return * this;
}

void BigInteger::trimZero() {
  vector<char>::reverse_iterator idx = number.rbegin();
  while (!number.empty() && (*idx) == 0) {
    number.pop_back();
    idx = number.rbegin();
  }
  if (number.size() == 0) {
    sign = true;
    number.push_back(0);
  }
}

BigInteger BigInteger::absolute() const {
  if (sign) {
    return * this;
  } else {
    return -(*this);
  }
}

BigInteger BigInteger::pow(int p) {
  BigInteger ex(1);
  for (int i = 0; i < p; ++i) {
    ex *= (*this);
  }
  return ex;
}

BigInteger operator+=(const BigInteger& num1, const BigInteger& num2) {
  if (num1.sign == num2.sign) {
    vector<char>::iterator idx1;
    vector<char>::const_iterator idx2;
    idx1 = num1.number.begin();
    idx2 = num2.number.begin();
    char ch = 0;
    while (idx1 != num1.number.end() && idx2 != num2.number.end()) {
      (*idx1) = (*idx1) + (*idx2) + ch;
      ch = ((*idx1) > 9);
      (*idx1) %= 10;
      ++idx1;
      ++idx2;
    }
    while (idx1 != num1.number.end()) {
      (*idx1) = (*idx1) + ch;
       ch = ((*idx1) > 9);
       (*idx1) %= 10;
       ++idx1;
    }
    while (idx2 != num2.number.end()) {
      char val = (*idx2) + ch;
      ch = (val > 9);
      val %= 10;
      num1.number.push_back(val);
      ++idx2;
    }
    if (ch != 0) {
      num1.number.push_back(ch);
      return num1;
    }
  } else {
    if (num1.sign) {
      return num1 -= (-num2);
    } else {
      return num1 = num2 - (-num1);
    }
  }
}

BigInteger operator-=(const BigInteger& num1, const BigInteger& num2) {
  if (num1.sign == num2.sign) {
    if (num1.sign) {
      if (num1 < num2) {
        return num1 = -(num2 - num1);
      }
    } else {
      if (-num1 > -num2) {
        return num1 = -((-num1) - (-num2));
      } else {
        return num1 = (-num2) - (-num1);
      }
    }
    vector<char>::iterator idx1;
    vector<char>::const_iterator idx2;
    idx1 = num1.number.begin();
    idx2 = num2.number.begin();
    char sub = 0;
    while (idx1 != num1.number.end() && idx2 != num2.number.end()) {
      (*idx1) = (*idx1) - (*idx2) - sub;
      sub = 0;
      if ((*idx1) < 0) {
        sub = 1;
        (*idx1) += 10;
      }
      ++idx1;
      ++idx2;
    }
    while (idx1 != num1.number.end()) {
      (*idx1) = (*idx1) - sub;
      sub = 0;
      if ((*idx1) < 0) {
        sub = 1;
        (*idx1) += 10;
      } else {
        break;
      }
      ++idx1;
    }
    num1.trimZero();
    return num1;
  } else {
    if (num1 > BigInteger::BigInteger(0)) {
      return num1 += (-num2);
    } else {
      return num1 = -(num2 + (-num1));
    }
  }
}

BigInteger operator*=(const BigInteger& num1, const BigInteger& num2) {
  BigInteger res(0);
  if (num1 == BigInteger::BigInteger(0) || num2 == BigInteger::BigInteger(0)) {
    res = BigInteger::BigInteger(0);
  } else {
    vector<char>::const_iterator idx2;
    idx2 = num2.number.begin();
    while (idx2 != num2.number.end()) {
      if ((*idx2) != 0) {
        deque<char> temp(num1.number.begin(), num1.number.end());
        char add = 0;
        deque<char>::iterator idx1;
        idx1 = temp.begin();
        while (idx1 != temp.end()) {
          (*idx1) *= (*idx2);
          (*idx1) += add;
          add = (*idx1) / 10;
          (*idx1) %= 10;
          ++idx1;
        }
        if (add != 0) {
          temp.push_back(add);
        }
        int count = idx2 - num2.number.begin();
        while (count--) {
          temp.push_front(0);
        }
        BigInteger temp2;
        temp2.number.insert(temp2.number.end(), temp.begin(), temp.end());
        temp2.trimZero();
        res += temp2;
      }
      ++idx2;
    }
    res.sign = ((num1.sign && num2.sign) || (!num1.sign && !num2.sign));
  }
  num1 = res;
  return num1;
}

BigInteger operator/=(const BigInteger& num1, const BigInteger& num2) {
  BigInteger snoob1 = num1.abs();
  BigInteger snoob2 = num2.abs();
  if (snoob1 < snoob2) {
    num1 = BigInteger::BigInteger(0);
    return num1;
  }
  deque<char> temp;
  vector<char>::reverse_iterator idx;
  idx = snoob1.number.rbegin();
  BigInteger temp2(0);
  while (idx != snoob1.number.rend()) {
    temp2 = temp2 * BigInteger::BigInteger(10) + BigInteger((int)(*idx));
    char divide = 0;
    while (temp2 >= snoob2) {
      temp2 -= snoob2;
      divide += 1;
    }
    temp.push_front(divide);
    ++idx;
  }
  num1.number.clear();
  num1.number.insert(num1.number.end(), temp.begin(), temp.end());
  num1.trimZero();
  num1.sign = ((num1.sign && num2.sign) || (!num1.sign && !num2.sign));
  return num1;
}

BigInteger operator%=(const BigInteger& num1, const BigInteger& num2) {
  return num1 -= ((num1 / num2) * num2);
}

BigInteger operator+(const BigInteger& num1, const BigInteger& num2) {
  BigInteger res(num1);
  res += num2;
  return res;
}

BigInteger operator++(BigInteger& num) {
  num += BigInteger::BigInteger(1);
  return num;
}

BigInteger operator++(BigInteger& num, int x) {
  BigInteger res(num);
  ++num;
  return res;
}

BigInteger operator-(const BigInteger& num1, const BigInteger& num2) {
  BigInteger res(num1);
  res -= num2;
  return res;
}

BigInteger operator-(const BigInteger& num) {
  BigInteger res = BigInteger(num);
  res.sign = !res.sign;
  return res;
}

BigInteger operator--(BigInteger& num) {
  num -= BigInteger::BigInteger(1);
  return num;
}

BigInteger operator--(BigInteger& num, int x) {
  BigInteger res(num);
  --num;
  return res;
}

BigInteger operator*(const BigInteger& num1, const BigInteger& num2) {
  BigInteger res(num1);
  res *= num2;
  return res;
}

BigInteger operator/(const BigInteger& num1, const BigInteger& num2) {
  BigInteger res(num1);
  res /= num2;
  return res;
}

BigInteger operator%(const BigInteger& num1, const BigInteger& num2) {
  BigInteger res(num1);
  res %= num2;
  return res;
}

bool operator>(const BigInteger&, const BigInteger&);
bool operator<(const BigInteger&, const BigInteger&);
bool operator>=(const BigInteger&, const BigInteger&);
bool operator<=(const BigInteger&, const BigInteger&);
bool operator!=(const BigInteger&, const BigInteger&);
bool operator==(const BigInteger&, const BigInteger&);
ostream& operator<<(ostream&, const BigInteger&);
istream& operator>>(istream&, BigInteger&);
