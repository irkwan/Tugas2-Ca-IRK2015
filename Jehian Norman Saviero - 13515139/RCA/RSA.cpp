/* Author	: Jehian Norman Saviero (@Reiva5) */
#include <bits/stdc++.h>
#include "RSA.h"

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

RSA::RSA(){
	n = q = 0;
	e = d = 19;
}

BigNumber RSA::get_n(){
	return n;
}

BigNumber RSA::get_e(){
	return e;
}

BigNumber RSA::get_d(){
	return d;
}
		
void RSA::generate(){
	if (data.empty()){
		ifstream in("prime_numbers.txt");
		BigNumber X;
		while (in >> X) data.pb(X);
	}
	BigNumber A, B;
	ll count = 1000;
	do {
		srand(time(NULL));
		A = modPow(rand()%data.size(),rand(),data.size());
		B = modPow(modPow(A,rand(),data.size()),A,data.size());
		--count;
	} while (A == B || count > 0);
	ll a = A.toInt();
	ll b = B.toInt();
	p = data[a];
	q = data[b];
}

void RSA::set_e(BigNumber E){
	e = E;
}

void RSA::process(){
	if (p == 0 || q == 0){
		generate();
	}
	n = p*q;
	BigNumber lambda_n = (p-1)*(q-1)/gcd(p-1,q-1);
	d = modInverse(e,n);
}

BigNumber RSA::encrypt(BigNumber m, BigNumber n, BigNumber e){
	return modPow(m,n,e);
}

BigNumber RSA::decrypt(BigNumber c, BigNumber d, BigNumber n){
	return modPow(c,d,n);
}

BigNumber RSA::encrypt(BigNumber message){
	return encrypt(message,n,e);
}

BigNumber RSA::decrypt(BigNumber encoded){
	return decrypt(encoded,d,n);
}
