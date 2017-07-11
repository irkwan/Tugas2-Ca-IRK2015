/* Author	: Jehian Norman Saviero (@Reiva5) */
#include <bits/stdc++.h>
#include "BigNumber.h"

/* MACROS SAMPAH */
#define jehian using
#define mau namespace
#define libur std
#define cepet {ios_base::sync_with_stdio(0);cin.tie(NULL);}

jehian mau libur;
/* MACROS FOR TYPE */
typedef long l;
typedef long double ld;
typedef long long ll;
typedef unsigned long ul;
typedef unsigned long long ull;
typedef vector<int> vi;
typedef vector<l> vl;
typedef vector<ll> vll;
typedef pair<int, int> pi;
typedef pair<l,l> pl;
typedef pair<ll,ll> pll;
typedef pair<ld,ld> pld;

/* MACROS FOR PRINT */
#define nl printf("\n");
#define sp printf(" ");

/* MACROS FOR FUNCTION */
#define pb push_back
#define mp make_pair
#define eb emplace_back


BigNumber::BigNumber(){
	number.pb(0);
	size = number.size();
	isNegate = false;
}

BigNumber::BigNumber(const BigNumber& A){
	number.clear();
	for (ll i = 0; i < A.number.size(); ++i){
		number.pb(A.number[i]);
	}
	size = A.number.size();
	isNegate = A.isNegate;
}

BigNumber::BigNumber(ll N){
	number.clear();
	if (N > 0){
		while (N > 0){
			number.pb(N%10);
			N /= 10;
		}
		isNegate = false;
	} else if (N == 0){
		number.pb(0);
		isNegate = false;
	} else {
		N *= -1;
		while (N > 0){
			number.pb(N%10);
			N /= 10;
		}
		isNegate = true;
	}
	size = number.size();
}

BigNumber::BigNumber(const string& A){
	number.clear();
	for (ll i = A.size() - 1; i >= 1; --i){
		number.pb(A[i] - 48);
	}
	if (A[0] == '-'){
		isNegate = true;
	} else {
		isNegate = false;
		number.pb(A[0] - 48);
	}
	size = number.size();
}

BigNumber& BigNumber::operator= (const BigNumber& A){
	number.clear();
	for (ll i = 0; i < A.number.size(); ++i){
		number.pb(A.number[i]);		
	}
	isNegate = A.isNegate;
	size = A.number.size();
	return *this;
}

BigNumber& BigNumber::operator= (ull A){
	number.clear();
	if (A != 0){
		if (A < 0){
			isNegate = true;
			A *= 1;
		} else {
			isNegate = false;
		}
		while (A){
			number.pb(A%10);
			A /= 10;
		}
	} else {
		isNegate = false;
		number.pb(0);
	}
	size = number.size();
	return *this;
}

BigNumber& BigNumber::operator= (const string& A){
	number.clear();
	for (ll i = A.size() - 1; i >= 1; --i){
		number.pb(A[i] - 48);
	}
	if (A[0] == '-'){
		isNegate = true;
	} else {
		isNegate = false;
		number.pb(A[0] - 48);
	}
	size = number.size();
	return *this;
}

istream& operator>> (istream& is, BigNumber& A){
	string x;
	is >> x;
	ll i;
	A.number.clear();
	for (i = x.size() - 1; i >= 1; --i){
		A.number.pb(x[i] - 48);
	}
	if (x[i] == '-'){
		A.isNegate = true;
	} else {
		A.isNegate = false;
		A.number.pb(x[i] - 48);
	}

	A.size = A.number.size();
	return is;
}

ostream& operator<< (ostream& os, const BigNumber& A){
	ll i;
	if (A.isNegate){
		os << "-";
	}
	for (i = A.number.size() - 1; i >= 0; --i){
		os << A.number[i];
	}
	return os;
}

bool operator==(const BigNumber& A, const BigNumber& B){
	return (A.number == B.number);
}

bool operator==(const BigNumber& A, const ll& B){
	BigNumber b(B);
	return (A.number == b.number);
}

bool operator==(const ll& A, const BigNumber& B){
	BigNumber a(A);
	return (a.number == B.number);
}

bool operator==(const BigNumber& A, const string& B){
	BigNumber b(B);
	return (A.number == b.number);
}

bool operator==(const string& A, const BigNumber& B){
	BigNumber a(A);
	return (a.number == B.number);
}

bool operator!=(const BigNumber& A, const BigNumber& B){
	return !(A.number == B.number);
}

bool operator!=(const BigNumber& A, const ll& B){
	BigNumber b(B);
	return !(A.number == b.number);
}

bool operator!=(const ll& A, const BigNumber& B){
	BigNumber a(A);
	return !(a.number == B.number);
}

bool operator!=(const BigNumber& A, const string& B){
	BigNumber b(B);
	return !(A.number == b.number);
}

bool operator!=(const string& A, const BigNumber& B){
	BigNumber a(A);
	return !(a.number == B.number);
}

bool operator<(const BigNumber& A, const BigNumber& B){
	vll numberA = A.number, numberB = B.number;
	reverse(numberA.begin(),numberA.end());
	reverse(numberB.begin(),numberB.end());
	if (!A.isNegate && !B.isNegate){
		if (A.number.size() < B.number.size()){
			return true;
		} else if (A.number.size() == B.number.size()){
			return numberA < numberB;
		} else {
			return false;
		}
	} else if (!A.isNegate && B.isNegate){
		return false;
	} else if (A.isNegate && !B.isNegate){
		return true;
	} else {
		if (A.number.size() > B.number.size()){
			return true;
		} else if (A.number.size() == B.number.size()){
			return numberA > numberB;
		} else {
			return false;
		}		
	}
}

bool operator<(const BigNumber& A, const ll& B){
	BigNumber b(B);
	return (A < b);
}

bool operator<(const ll& A, const BigNumber& B){
	BigNumber a(A);
	return (a < B);
}

bool operator<(const BigNumber& A, const string& B){
	BigNumber b(B);
	return (A < b);
}

bool operator<(const string& A, const BigNumber& B){
	BigNumber a(A);
	return (a < B);
}

bool operator>(const BigNumber& A, const BigNumber& B){
	vll numberA = A.number, numberB = B.number;
	reverse(numberA.begin(),numberA.end());
	reverse(numberB.begin(),numberB.end());
	if (!A.isNegate && !B.isNegate){
		if (A.number.size() > B.number.size()){
			return true;
		} else if (A.number.size() ==  B.number.size()){
			return numberA > numberB;
		} else {
			return false;
		}
	} else if (!A.isNegate && B.isNegate){
		return true;
	} else if (A.isNegate && !B.isNegate){
		return false;
	} else {
		if (A.number.size() < B.number.size()){
			return true;
		} else if (A.number.size() == B.number.size()){
			return numberA < numberB;
		} else {
			return false;
		}
	}
}

bool operator>(const BigNumber& A, const ll& B){
	BigNumber b(B);
	return (A > b);
}

bool operator>(const ll& A, const BigNumber& B){
	BigNumber a(A);
	return (a > B);
}

bool operator>(const BigNumber& A, const string& B){
	BigNumber b(B);
	return (A > b);
}

bool operator>(const string& A, const BigNumber& B){
	BigNumber a(A);
	return (a > B);
}

bool operator<=(const BigNumber& A, const BigNumber& B){
	return A == B || A < B;
}

bool operator<=(const BigNumber& A, const ll& B){
	BigNumber b(B);
	return (A <= b);
}

bool operator<=(const ll& A, const BigNumber& B){
	BigNumber a(A);
	return (a <= B);
}

bool operator<=(const BigNumber& A, const string& B){
	BigNumber b(B);
	return (A <= b);
}

bool operator<=(const string& A, const BigNumber& B){
	BigNumber a(A);
	return (a <= B);
}

bool operator>=(const BigNumber& A, const BigNumber& B){
	return A == B || A > B;
}

bool operator>=(const BigNumber& A, const ll& B){
	BigNumber b(B);
	return (A >= b);
}

bool operator>=(const ll& A, const BigNumber& B){
	BigNumber a(A);
	return (a >= B);
}

bool operator>=(const BigNumber& A, const string& B){
	BigNumber b(B);
	return (A >= b);
}

bool operator>=(const string& A, const BigNumber& B){
	BigNumber a(A);
	return (a >= B);
}

BigNumber operator+(const BigNumber& A, const BigNumber& B){
	if (!A.isNegate && !B.isNegate){
		BigNumber C;
		C.number.clear();
		ll i;
		ll carry = 0;
		ll MIN = min(A.number.size(), B.number.size());
		for (i = 0; i < MIN; ++i){
			C.number.pb((A.number[i] + B.number[i] + carry) % 10);
			carry = (A.number[i] + B.number[i] + carry) > 9;
		}
		ll MAX = max(A.number.size(), B.number.size());
		bool goB, goA;
		goB = B.number.size() > A.number.size();
		goA = A.number.size() > B.number.size();
		for (; i < MAX && (goA || goB); ++i){
			if (goB){
				C.number.pb((B.number[i]+carry) % 10);
				carry = (B.number[i] + carry) > 9;
			} else if (goA){
				C.number.pb((A.number[i]+carry) % 10);
				carry = (A.number[i] + carry) > 9;
			}
		}
		if (carry){
			C.number.pb(carry);
		}
		while (!C.number.back() && C.number.size() > 1){
			C.number.pop_back();
		}
		C.size = C.number.size();
		return C;
	} else if (!A.isNegate && B.isNegate){
		BigNumber b = B;
		b.isNegate = false;
		return A - b;
	} else if (A.isNegate && !B.isNegate){
		BigNumber a = A;
		a.isNegate = false;
		return B - a;
	} else {
		BigNumber C;
		C.number.clear();
		BigNumber a = A, b = B;
		a.isNegate = false;
		b.isNegate = false;
		C = a + b;
		C.isNegate = true;
		return C;
	}
}

BigNumber& BigNumber::operator+=(const BigNumber& A){
	*this = *this + A;
	return *this;
}

BigNumber operator-(const BigNumber& X, const BigNumber& Y){
	BigNumber A, B, C;
	A = X;
	B = Y;
	if (!A.isNegate && !B.isNegate){
		if (X >= Y){
			A = X;
			B = Y;
		} else {
			B = X;
			A = Y;
			C.isNegate = true;
		}
		C.number.clear();
		ll i = 0;
		ll MIN = min(A.number.size(), B.number.size());
		ll MAX = max(A.number.size(), B.number.size());
		for (i = 0; i < MIN; ++i){
			if (A.number[i] >= B.number[i]){
				C.number.pb(A.number[i] - B.number[i]);
			} else {
				--A.number[i+1];
				C.number.pb(A.number[i] - B.number[i] + 10);
			}
		}
		bool goA, goB;
		goA = A.number.size() > B.number.size();
		goB = B.number.size() > A.number.size();
		for(; i < MAX && (goA || goB); ++i){
			if (goB){
				if (B.number[i] < 0){
					C.number.pb(B.number[i] + 10);
					--B.number[i+1];
				} else {
					C.number.pb(B.number[i]);
				}
			} else if (goA){
				if (A.number[i] < 0){
					C.number.pb(A.number[i] + 10);
					--A.number[i+1];
				} else {
					C.number.pb(A.number[i]);
				}
			}
		}
		while (C.number.back() == 0 && C.number.size() > 1){
			C.number.pop_back();
		}
		C.size = C.number.size();
		return C;
	} else if (!A.isNegate && B.isNegate){
		B.isNegate = false;
		return A + B;
	} else if (A.isNegate && !B.isNegate){
		A.isNegate = false;
		C = A + B;
		BigNumber neg(-1);
		C = C * neg;
		return C;
	} else {
		B.isNegate = false;
		A.isNegate = false;
		C = A - B;
		C.isNegate = !C.isNegate;
		return C;
	}
}

BigNumber& BigNumber::operator-=(const BigNumber& A){
	*this = *this - A;
	return *this;
}

BigNumber operator*(const BigNumber& A, const BigNumber& B){
	BigNumber C;
	C.number.clear();
	C.isNegate = A.isNegate ^ B.isNegate;
	C.number.resize(A.number.size() + B.number.size(), 0);
	C.size = C.number.size();
	for (ll i = 0; i < A.number.size(); ++i){
		for (ll j = 0; j < B.number.size(); ++j){
			C.number[i + j] = C.number[i+j] + (A.number[i] * B.number[j]);
		}
	}

	ll carry = 0;
	for (ll i = 0; i < C.number.size(); ++i){
		ll next_carry = ((C.number[i] + carry) / 10);
		C.number[i] = (C.number[i] + carry) % 10;
		carry = next_carry;
	}
	while (carry > 0){
		C.number.pb(carry%10);
		carry /= 10;
	}
	while (!C.number.back() && C.number.size() > 1){
		C.number.pop_back();
	}
	C.size = C.number.size();
	return C;
}

BigNumber& BigNumber::operator*=(const BigNumber& A){
	*this = *this * A;
	return *this;
}

BigNumber operator/(BigNumber B, BigNumber A){
	BigNumber C;
	C.number.clear();
	C.isNegate = A.isNegate ^ B.isNegate;
	BigNumber temp;
	temp.number.clear();
	/* PRE-COMPUTE GET AMOUNT OF NUMBER */
	if (B.number.size() >= A.number.size()){
		ll i = A.number.size();
		while (i--){
			temp.number.pb(B.number.back());
			B.number.pop_back();
		}
		BigNumber junk = temp;
		reverse(junk.number.begin(),junk.number.end());
		if (junk < A && B.number.size() > 0){
			temp.number.pb(B.number.back());
			B.number.pop_back();
		}
		reverse(temp.number.begin(),temp.number.end());
		ll count = 0;
		while (temp >= A){
			temp -= A;
			++count;
		}
		C.number.pb(count);
	}

	/* ITERATION UNTIL B < A */
	while (B.number.size() > 0){
		temp.number.insert(temp.number.begin(),B.number.back());
		B.number.pop_back();
		while (!temp.number.back() && temp.number.size() > 1){
			temp.number.pop_back();
		}
		ll count = 0;
		while (temp >= A){
			temp -= A;
			++count;
		}
		C.number.pb(count);
	}
	reverse(C.number.begin(), C.number.end());
	while (!C.number.back() && C.number.size() > 1){
		C.number.pop_back();
	}
	C.size = C.number.size();
	return C;
}

BigNumber& BigNumber::operator/=(const BigNumber& A){
	*this = *this / A;
	return *this;
}

BigNumber operator%(BigNumber B, BigNumber A){
	BigNumber temp;
	temp.isNegate = A.isNegate ^ B.isNegate;
	temp.number.clear();
	/* PRE-COMPUTE GET AMOUNT OF NUMBER */
	if (B.number.size() >= A.number.size()){
		ll i = A.number.size();
		while (i--){
			temp.number.pb(B.number.back());
			B.number.pop_back();
		}
		BigNumber junk = temp;
		reverse(junk.number.begin(),junk.number.end());
		if (junk < A && B.number.size() > 0){
			temp.number.pb(B.number.back());
			B.number.pop_back();
		}
		reverse(temp.number.begin(),temp.number.end());
		while (temp >= A){
			temp -= A;
		}
	}

	/* ITERATION UNTIL B < A */
	while (B.number.size() > 0){
		temp.number.insert(temp.number.begin(),B.number.back());
		B.number.pop_back();
		while (!temp.number.back() && temp.number.size() > 1){
			temp.number.pop_back();
		}
		while (temp >= A){
			temp -= A;
		}
	}
	while (!temp.number.back() && temp.number.size() > 1){
		temp.number.pop_back();
	}
	temp.size = temp.number.size();
	if (temp.isNegate){
		return temp + A;
	} else {
		return temp;
	}
}

BigNumber& BigNumber::operator%=(const BigNumber& A){
	*this = *this % A;
	return *this;
}

BigNumber gcd(BigNumber A, BigNumber B){
	if (A < 0) A *= -1;
	if (B < 0) B *= -1;
	BigNumber zero = 0;
	while (A % B > zero){
		A %= B;
		swap(A,B);
	}
	return B;
}

void swap(BigNumber &A, BigNumber &B){
	BigNumber C = A;
	A = B;
	B = C;
}

BigNumber gcdExtended(BigNumber a, BigNumber b, BigNumber &x, BigNumber &y){
	// Kasus dasar
	if (a == 0){
		x = 0, y = 1;
		return b;
	}

	BigNumber x1, y1;
	BigNumber gcd = gcdExtended(b%a, a, x1, y1);

	// Update x dan y dengan menggunakan hasil rekursif
	x = y1 - (b/a) * x1;
	y = x1;
	return gcd;
}

BigNumber modInverse(BigNumber a, BigNumber m){
	BigNumber x, y;
	BigNumber g = gcdExtended(a, m, x, y);
	if (g != 1)
		throw runtime_error("Tolong lah, GCD(a,m) != 1 please");
	else
		return (x%m + m) % m;
}

BigNumber pow(BigNumber a, BigNumber b){
	if (b <= 2){
		if (b == 2){
			return a*a;
		} else if (b == 1){
			return a;
		} else {
			return 1;
		}
	} else {
		if (b % 2 == 0) return pow(pow(a,b/2),2);
		else return a*pow(pow(a,(b-1)/2),2);
	}
}

BigNumber modPow(BigNumber a, BigNumber b, BigNumber m){
	if (b <= 2){
		if (b == 2){
			return (a*a)%m;
		} else if (b == 1){
			return a%m;
		} else {
			return 1;
		}
	} else {
		if (b % 2 == 0) return modPow(modPow(a,b/2,m)%m,2,m)%m;
		else return (a*(modPow(modPow(a,(b-1)/2,m)%m,2,m)%m))%m;
	}
}