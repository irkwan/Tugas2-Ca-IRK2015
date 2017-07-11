/* Author	: Jehian Norman Saviero (@Reiva5) */
#include <bits/stdc++.h>

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

#ifndef BIGNUMBER_H
#define BIGNUMBER_H

class BigNumber{
	public:
		/* Ctor */
		BigNumber();

		/* CCtor */
		BigNumber(const BigNumber& A);
		BigNumber(ll N);
		BigNumber(const string& A);
		
		/* Operator = */
		BigNumber& operator= (const BigNumber& A);
		BigNumber& operator= (ull A);
		BigNumber& operator= (const string& A);

		/* Operator input - output */
		friend istream& operator>> (istream& is, BigNumber& A);
		friend ostream& operator<< (ostream& os, const BigNumber& A);

		/* Operator comparator */
		/** Operator == **/
		friend bool operator==(const BigNumber& A, const BigNumber& B);
		friend bool operator==(const BigNumber& A, const ll& B);
		friend bool operator==(const ll& A, const BigNumber& B);
		friend bool operator==(const BigNumber& A, const string& B);
		friend bool operator==(const string& A, const BigNumber& B);

		/** Operator != **/
		friend bool operator!=(const BigNumber& A, const BigNumber& B);
		friend bool operator!=(const BigNumber& A, const ll& B);
		friend bool operator!=(const ll& A, const BigNumber& B);
		friend bool operator!=(const BigNumber& A, const string& B);
		friend bool operator!=(const string& A, const BigNumber& B);

		/** Operator < **/
		friend bool operator<(const BigNumber& A, const BigNumber& B);
		friend bool operator<(const BigNumber& A, const ll& B);
		friend bool operator<(const ll& A, const BigNumber& B);
		friend bool operator<(const BigNumber& A, const string& B);
		friend bool operator<(const string& A, const BigNumber& B);

		/** Operator > **/
		friend bool operator>(const BigNumber& A, const BigNumber& B);
		friend bool operator>(const BigNumber& A, const ll& B);
		friend bool operator>(const ll& A, const BigNumber& B);
		friend bool operator>(const BigNumber& A, const string& B);
		friend bool operator>(const string& A, const BigNumber& B);

		/** Operator <= **/
		friend bool operator<=(const BigNumber& A, const BigNumber& B);
		friend bool operator<=(const BigNumber& A, const ll& B);
		friend bool operator<=(const ll& A, const BigNumber& B);
		friend bool operator<=(const BigNumber& A, const string& B);
		friend bool operator<=(const string& A, const BigNumber& B);

		/** Operator >= **/
		friend bool operator>=(const BigNumber& A, const BigNumber& B);
		friend bool operator>=(const BigNumber& A, const ll& B);
		friend bool operator>=(const ll& A, const BigNumber& B);
		friend bool operator>=(const BigNumber& A, const string& B);
		friend bool operator>=(const string& A, const BigNumber& B);

		/** Operator + **/
		friend BigNumber operator+(const BigNumber& A, const BigNumber& B);
		BigNumber& operator+=(const BigNumber& A);

		/** Operator - **/
		friend BigNumber operator-(const BigNumber& X, const BigNumber& Y);
		BigNumber& operator-=(const BigNumber& A);

		/** Operator * **/
		friend BigNumber operator*(const BigNumber& A, const BigNumber& B);
		BigNumber& operator*=(const BigNumber& A);

		/** Operator / **/
		friend BigNumber operator/(BigNumber B, BigNumber A);
		BigNumber& operator/=(const BigNumber& A);

		/** Operator % **/
		friend BigNumber operator%(BigNumber B, BigNumber A);
		BigNumber& operator%=(const BigNumber& A);

		
	private:
		vll number;
		ull size;
		bool isNegate;
};
#endif

/** ADT **/
/*** FUNGSI GCD ***/
/* Mencari FPB dari dua buah bilangan A dan B */
BigNumber gcd(BigNumber A, BigNumber B);

/*** FUNGSI SWAP ***/
/* Menukar dua buah bilangan A dan B */
void swap(BigNumber &A, BigNumber &B);

/*** EXTENDED ECULDIAN ALGORITHM ***/
/* Mencari solusi yang mungkin sehingga ax + by = 1 */
BigNumber gcdExtended(BigNumber a, BigNumber b, BigNumber &x, BigNumber &y);

/*** MOD INVERSE ***/
/* Akan dicari sebuah nilai b sehingga (a*b)%m == 1 */
BigNumber modInverse(BigNumber a, BigNumber m);