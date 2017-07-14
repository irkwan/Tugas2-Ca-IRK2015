/* Author	: Jehian Norman Saviero (@Reiva5) */
#include <bits/stdc++.h>
#include "../BigNumber/BigNumber.h"
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

int main(){
	RSA yes;
	yes.process();
	cout << yes.p << " " << yes.q << endl;
	cout << "=========FINISH=========";
	return 0;
}