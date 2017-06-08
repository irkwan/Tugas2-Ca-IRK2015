// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger.h

#include <iostream>
#include <vector>
#include <string>
using namespace std;

class biginteger {
public:
	/* Constructors */
	biginteger();
	biginteger(long long v);
	biginteger(const string &v);
	biginteger(const biginteger &v);

	/* Destructor */
	~biginteger();

	/* Operator= */
	biginteger& operator=(const biginteger &rhs);

	/* Arithmetic Operators */
	biginteger add(const biginteger &rhs);
	biginteger min(const biginteger &rhs);
	biginteger mul(const biginteger &rhs);
	biginteger div(const biginteger &rhs);
	biginteger mod(const biginteger &rhs);

	biginteger operator+(const biginteger &rhs);
	biginteger operator-(const biginteger &rhs);
	biginteger operator*(const biginteger &rhs);
	biginteger operator/(const biginteger &rhs);
	biginteger operator%(const biginteger &rhs);

	/* Relational Operators */
	bool operator==(const biginteger &rhs);
	bool operator>(const biginteger &rhs);
	bool operator<(const biginteger &rhs);

private:
	vector<char> digits;
	bool sign;
};