/*
Author : Arno Alexander
*/

#include "BigNumber.h"
#include "RSA.h"

using namespace std;

int main() {
	unsigned le;
	cin >> le;
	BigNumber b = BigNumber::generateProbablePrime(le);
	cout << b << endl;
	b = b.nextProbablePrime();
	cout << b << endl;
	return 0;
}