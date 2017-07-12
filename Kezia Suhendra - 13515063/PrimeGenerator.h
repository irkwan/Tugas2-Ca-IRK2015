//NIM      : 13515063
//Nama     : Kezia Suhendra

#ifndef PRIMEGEN_H_
#define PRIMEGEN_H_

#include "BigInteger.h"

void GenPrime(BigInt& n, int digit);
bool RabinMiller(const BigInt& n, int digit);
BigInt GeneratePrime(int digit);

#endif
