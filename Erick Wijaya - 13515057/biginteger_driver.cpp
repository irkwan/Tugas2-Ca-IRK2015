// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger_driver.cpp

#include "biginteger.h"
#include <iostream>
#include <vector>
#include <string>
using namespace std;

int main() {
	biginteger b1("12345");
	biginteger b2("-832364374313221321434234324");
	biginteger b3 = b2;
	biginteger b4;

	cout << b1 << " " << b2 << " " << b3 << " " << b4 << endl;

	return 0;
}
