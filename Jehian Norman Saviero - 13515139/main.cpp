/* Author	: Jehian Norman Saviero (@Reiva5) */
#include <bits/stdc++.h>
#include "BigNumber/BigNumber.h"
#include "RSA/RSA.h"

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

time_t mulai, gen, enkripsi, dekripsi;
int main(){
	string input, decrypt, encrypt;
	cout << "Masukkan nama file input                : "; cin >> input;
	cout << "Masukkan nama file output untuk enkripsi: "; cin >> encrypt;
	cout << "Masukkan nama file output untuk dekripsi: "; cin >> decrypt;
	cout << "MULAI GENERATE KEY\n";
	cout << "======================================================";
	mulai = clock();
	RSA rsa;
	rsa.process();
	cout << "  Done\n";
	gen = clock();
	cout << "ENKRIPSI FILE\n";
	cout << "======================================================";
	rsa.encrypt_to_code(input, encrypt);
	enkripsi = clock();
	cout << " Done\n";
	cout << "DEKRIPSI FILE\n";
	cout << "======================================================";
	rsa.decrypt_to_normal(encrypt, decrypt);
	dekripsi = clock();
	cout << " Done\n";
	nl; nl; nl;
	cout << "=====================STATS RESULT=====================\n";
	cout << "Time elapsed (generate)            : " << (ld) (gen-mulai)/CLOCKS_PER_SEC << " s" << endl;
	cout << "Time elapsed (enkripsi)            : " << (ld) (enkripsi-gen)/CLOCKS_PER_SEC << " s" << endl;
	cout << "Time elapsed (dekripsi)            : " << (ld) (dekripsi-enkripsi)/CLOCKS_PER_SEC << " s" << endl;
	cout << "Time total                         : " << (ld) (dekripsi-mulai)/CLOCKS_PER_SEC << " s" << endl;
	cout << "Bilangan prima pertama             : " << rsa.p << endl;
	cout << "Bilangan prima kedua               : " << rsa.q << endl;
	cout << "Key n (Public)                     : " << rsa.get_n() << endl;
	cout << "Key e (Public)                     : " << rsa.get_e() << endl;
	cout << "Key d (Secret)                     : " << rsa.get_d() << endl;
	return 0;
}