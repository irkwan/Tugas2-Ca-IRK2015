// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger_driver.cpp

#include "biginteger.h"
#include <iostream>
#include <vector>
#include <string>
using namespace std;

int main() {
	/*biginteger b1(54321);
	biginteger b2(987);
	biginteger b3(77777);
	biginteger b4(123456);

	biginteger b5(-17364324723847238LL);
	biginteger b6(0);
	biginteger b7("1");
	biginteger b8("-2");

	biginteger b9(1);
	biginteger b10(-2);
	biginteger b11 = b8;
	biginteger b12 = b10;

	cout << b1 << " " << b2 << " " << b3 << " " << b4 << endl;
	cout << b5 << " " << b6 << " " << b7 << " " << b8 << endl;
	cout << b9 << " " << b10 << " " << b11 << " " << b12 << endl << endl;

	cout << b1 + b2 << endl;
	cout << b1 + b3 << endl;
	cout << b1 + b4 << endl;

	cout << b1 - b2 << endl;
	cout << b1 - b3 << endl;
	cout << b1 - b4 << endl;

	cout << b1 % b2 << " " << 54321%987 << endl;
	cout << b3 % b1 << endl;
	cout << b4 % b8 << endl;*/
	biginteger b1, b2;

	cin >> b1 >> b2;
	cout << "b1 + b2 = " << b1 + b2 << endl;
	cout << "b1 - b2 = " << b1 - b2 << endl;
	cout << "b1 / b2 = " << b1 / b2 << endl;
	cout << "b1 %% b2 = " << b1 % b2 << endl;
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
	//cout << b1 + b2 << endl;
	//cout << b1 + b2 << endl;

	return 0;
}
