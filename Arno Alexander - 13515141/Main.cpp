/*
Author : Arno Alexander
*/

#include <iostream>
#include <deque>
#include <cmath>
#include <utility>
#include "BigNumber.h"

using namespace std;

int main() {
	pair<BigNumber,BigNumber> res = unsignedDivide(BigNumber("99999999912312345700000000000000000000000000000000000000000000000000000"),BigNumber("1999999999999999999"));
	cout << res.first << " " << res.second << endl;
	return 0;
}