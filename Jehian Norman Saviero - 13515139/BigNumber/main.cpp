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

vector<BigNumber> data;
vector<string> menu;
BigNumber A, B;
void display_menu(){
	if (!menu.empty()) for (auto s : menu) cout << s << endl;
	else {
		ifstream in("menu.txt");
		string temp;
		while (getline(in,temp)){
			menu.pb(temp);
		}
		display_menu();
	}
}

int main(){
	ll size;
	cout << "Test Library, untuk menguji unit ini akan digunakan dua buah bilangan\n";
	cout << "Masukkan bilangan A: "; cin >> A;
	cout << "Masukkan bilangan B: "; cin >> B;
/*	data.resize(size);
	for (ll i = 1; i <= size; ++i){
		cout << "Masukkan data ke-" << i << ": "; cin >> data[i];
	}
*/
	ll menu = 0;
	do
	{	
		display_menu();
		cout << "Masukkan pilihan menu: "; cin >> menu;
		if (menu == 1) cout << "Hasil A + B adalah: " << A + B << endl;
		else if (menu == 2) cout << "Hasil A - B adalah: " << A - B << endl;
		else if (menu == 3) cout << "Hasil A * B adalah: " << A * B << endl;
		else if (menu == 4) cout << "Hasil A / B adalah: " << A / B << endl;
		else if (menu == 5) cout << "Hasil A mod B adalah: " << A % B << endl;
		else if (menu == 6){
			string ganti;
			cout << "Mau ganti nilai A? (Y/N): "; cin >> ganti;
			if (ganti == "Y" || ganti == "y") cout << "Masukkan nilai A: ", cin >> A;
			ganti.clear();
			cout << "Mau ganti nilai B? (Y/N): "; cin >> ganti;
			if (ganti == "Y" || ganti == "y") cout << "Masukkan nilai B: ", cin >> B;
			ganti.clear();
		} else if (menu == 7) cout << "GCD(A,B) adalah: " << gcd(A,B) << endl; 
		else if (menu) {
			cout << "Menu tidak tersedia" << endl;
		}
	} while (menu);
	cout << "Terima kasih telah mencoba library ini, laporkan bug bila dikemudian hari ditemukan kesalahan =/\\=";
	return 0;
}