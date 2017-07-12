//NIM      : 13515063
//Nama     : Kezia Suhendra

#include "OAEP.h"
#include <iostream>
#include <string.h>
#include "BigInteger.h"
#include "Key.h"
#include <ctime>
#include "UtilsMsg.h"
#include <cstdio>
#include <algorithm>

using namespace std;

BigInt OAEP::KtoM(const BigInt& cnt) {
  BigInt temp(cnt);
  BigInt res(cnt);
  while (res.GetBitLength() < M) {
    temp = (temp << 1);
    res = (res ^ temp);
  }
  return res;
}

BigInt OAEP::MtoK(const BigInt& cnt) {
  BigInt filter(1);
  BigInt res(0);
  BigInt temp(cnt);
  for (int i = 0; i < K / 16; i++) {
    filter = (filter << 16);
  }
  filter = (filter << (K % 16));
  filter = filter - 1;
  res = filter;
  filter = (filter >> 1);
  while (temp.GetBitLength() >= K) {
    res = (filter & temp) ^ res;
    temp = (temp >> 1);
  }
  return res;
}

OAEP::OAEP(int k, int m) {
  this->K = k;
  this->M = m;
}

BigInt OAEP::encode(const BigInt& cnt) {
  if (cnt.GetBitLength() > M) {
    fprintf(stderr, "Message out of range. Length is %d\n", cnt.GetBitLength());
    exit(EXIT_FAILURE);
  }
  BigInt r, temp, P1, P2;
  srand((unsigned) time(NULL));
  r.Random(K);
  temp = KtoM(r);
  P1 = cnt ^ temp;
  temp = MtoK(P1);
  P2 = temp ^ r;
  for (int i = 0; i < K / 16; i++) {
    P1 = (P1 << 16);
  }
  P1 = (P1 << (K % 16));
  P1 = P1 | P2;
  return P1;
}

void OAEP::encode(StringTrans& st) {
  for (StringTrans::iterator itr = st.begin(); itr != st.end(); ++itr) {
    *itr = encode(*itr);
  }
}

BigInt OAEP::decode(const BigInt& cnt) {
  BigInt r, temp, P2, res;
  BigInt P1(cnt);
  BigInt filter(1);
  for (int i = 0; i < K / 16; i++) {
    filter = (filter << 16);
  }
  filter = (filter << (K % 16));
  filter = filter - 1;
  P2 = (cnt & filter);
  for (int i = 0; i < K / 16; i++) {
    P1 = (P1 >> 16);
  }
  P1 = (P1 >> (K % 16));
  temp = MtoK(P1);
  r = (temp ^ P2);
  temp = KtoM(r);
  res = (temp ^ P1);
  return res;
}

void OAEP::decode(StringTrans& st) {
  for (StringTrans::iterator itr = st.begin(); itr != st.end(); ++itr) {
    *itr = decode(*itr);
  }
}
