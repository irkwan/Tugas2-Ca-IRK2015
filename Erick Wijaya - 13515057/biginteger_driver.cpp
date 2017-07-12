// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger_driver.cpp

#include "biginteger.h"
#include <iostream>
#include <vector>
#include <string>
using namespace std;

int main() {
	biginteger b1, b2(2), b3(2);
	//cin >> b1 >> b2 >> b3;

	cout << "b1 + b2 = " << b1 + b2 << endl;
	cout << "b1 - b2 = " << b1 - b2 << endl;
	cout << "b1 * b2 = " << b1 * b2 << endl;
	cout << "b1 / b2 = " << b1 / b2 << endl;
	cout << "b1 % b2 = " << b1 % b2 << endl;
	cout << endl;
	cout << "b1 > b2 = " << (b1 > b2) << endl;
	cout << "b1 >= b2 = " << (b1 >= b2) << endl;
	cout << "b1 < b2 = " << (b1 < b2) << endl;
	cout << "b1 <= b2 = " << (b1 <= b2) << endl;
	cout << "b1 == b2 = " << (b1 == b2) << endl;
	cout << "b1 != b2 = " << (b1 != b2) << endl;
	cout << endl;
	cout << "abs(b1) abs(b2) = " << b1.abs() << " " << b2.abs() << endl;
	cout << "-b1 -b2 = " << -b1 << " " << -b2 << endl;
	cout << endl;
	cout << "gcd(b1,b2) = " << biginteger::gcd(b1, b2) << endl;
	biginteger x, y;
	cout << "b1*x + b2*y = (the result) " << biginteger::gcdExtended(b1, b2, x, y) << endl;
	cout << "x = " << x << " and y = " << y << endl;
	cout << "b1*x + b2*y = (expected)   " << b1*x + b2*y << endl; 
	cout << endl;
	//cout << "pow(b1,b2) = " << biginteger::pow(b1, b2) << endl;
	cout << "modpow(b1,b2,b3) = " << biginteger::modpow(b1, b2, b3) << endl;
	cout << "random prime = " << biginteger::generateRandomProbablePrime() << endl;
	biginteger myint(126);
	cout << myint.toInt() << " " << myint.toString() << endl;


	return 0;
}
