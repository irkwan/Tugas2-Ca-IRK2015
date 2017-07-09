/*
Author : Arno Alexander
*/

#include "BigNumber.h"
#include "RSA.h"

using namespace std;

int main() {
	string a = "a.txt", b = "b.txt", c = "c.txt" , d = "d.txt" , e = "e.txt";
	RSA rsa(20);
	cout << "encrypting" << endl;
	rsa.encrypt(a,b);
	cout << "decrypting" << endl;
	rsa.decrypt(b,c);
	cout << "encrypting" << endl;
	rsa.encrypt(c,d);
	cout << "decrypting" << endl;
	rsa.decrypt(d,e);
	return 0;
}