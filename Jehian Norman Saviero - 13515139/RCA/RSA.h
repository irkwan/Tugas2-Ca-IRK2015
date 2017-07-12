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

#ifndef RSA_H
#define RSA_H
#include "../BigNumber/BigNumber.h"

class RSA {
	public:
		/* CTOR */
		RSA();

		/* GETTER */
		BigNumber get_n();
		BigNumber get_e();
		BigNumber get_d();

		/* SETTE */
		void set_e(BigNumber E); //Public key (Part II)

		/* METHOD */
		void generate();
		void process();
		BigNumber encrypt(BigNumber message);
		BigNumber decrypt(BigNumber encoded);

		BigNumber p;
		BigNumber q;
	private:
		BigNumber encrypt(BigNumber m, BigNumber n, BigNumber e);
		BigNumber decrypt(BigNumber c, BigNumber d, BigNumber n);
		BigNumber n; //Public key (Part I)
		BigNumber e; //Public key (Part II)
		BigNumber d; //Private key d = e^-1 mod λ(n)
		vector<BigNumber> data;
};
#endif