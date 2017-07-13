//NIM      : 13515063
//Nama     : Kezia Suhendra

#include "StringTrans.h"
#include <string>
#include <vector>
#include "BigInteger.h"
#include "OAEP.h"
#include "RSA.h"
#include "UtilsMsg.h"

using namespace std;

void StringTrans::split(const string& msg) {
  int n = 0;
  int i = 0;
  int j = 0;
  int StrLen = BitLen / 8;
  (*this).clear();
  const char* msg_c = msg.c_str();
  do {
    char seg[255];
    for (i = n * StrLen, j = 0; i < msg.length() && i < (n + 1) * StrLen; i++, j++) {
      seg[j] = msg_c[i];
    }
    seg[j] = '\0';
    string segment(seg);
    vector<BigInt>::push_back(BigInt(segment, 10));
    n++;
  } while (n * StrLen < msg.length());
}

StringTrans::StringTrans(const string& a, int b) {
  this->BitLen = b;
  this->split(a);
}

string StringTrans::toString() {
  int len = this->size();
  string res, temp;
  for (int i = 0; i < len; i++) {
    temp = (*this)[i].ToString();
    res = res + temp;
  }
  return res;
}

string StringTrans::toHexString() {
  int len = this->size();
  string res, temp;
  for (int i = 0; i < len; i++) {
    temp = (*this)[i].ToHexString();
    res = res + temp;
  }
  return res;
}

void StringTrans::push_back(const string& str) {
  this->push_back(str);
}

void StringTrans::push_back(const BigInt& big) {
  vector<BigInt>::push_back(big);
}

StringTrans& StringTrans::operator+= (const string& str) {
  this->push_back(str);
  return *this;
}

StringTrans& StringTrans::operator+= (const BigInt& big) {
  vector<BigInt>::push_back(big);
  return *this;
}

StringTrans& StringTrans::operator+= (const StringTrans& st) {
  for (StringTrans::const_iterator itr = st.begin(); itr != st.end(); itr++) {
    vector<BigInt>::push_back(*itr);
  }
  return *this;
}
