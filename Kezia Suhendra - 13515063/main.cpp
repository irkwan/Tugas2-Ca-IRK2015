//NIM      : 13515063
//Nama     : Kezia Suhendra

#include <string>
#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <fstream>
#include "BigInteger.h"
#include "StringTrans.h"
#include "RSA.h"
#include "OAEP.h"
#include <ctime>

using namespace std;

int main(int argc, char **argv) {
  if (argc != 3) {
    printf("How to execute: ./<exec> <either 512 / 768 / 1024 / 2048> <file name>");
    exit(EXIT_FAILURE);
  }
  int keyLength;
  keyLength = atoi(argv[1]);
  if (keyLength < 256) {
    printf("Input the number either 512 or 768 or 1024 or 2048 after ./<exec>\n");
    exit(EXIT_FAILURE);
  }
  switch(keyLength) {
    case 512:
    case 768:
    case 1024:
    case 2048:
      break;
    default:
      printf("Input the number either 512 or 768 or 1024 or 2048 after ./<exec>\n");
      exit(EXIT_FAILURE);
  }
  ifstream input(argv[2]);
  string msg;
  char c;
  while (!input.eof()) {
    c = input.get();
    if (c != '\0') {
      msg += c;
    }
  }
  input.close();
  msg.pop_back();
  RSA *rsa;
  auto begin = chrono::high_resolution_clock::now();
  rsa = new RSA(keyLength);
  BigInt N, E;
  rsa->getPublicKey(N, E);
  StringTrans st(msg, N.GetBitLength() - 17);
  cout << "Input Message: " << endl;
  cout << st.toString() << endl;
  OAEP oaep(16, N.GetBitLength() - 17);
  oaep.encode(st);
  RSA::encrypt(st, N, E);
  cout << "\nEncrypted Message:\n";
  cout << st.toHexString() << endl;
  auto end = chrono::high_resolution_clock::now();
  rsa->decrypt(st);
  cout << "\nDecryped Message:\n";
  cout << st.toHexString() << endl;
  oaep.decode(st);
  cout << "\nDecoded Message:\n";
  cout << st.toString() << endl << endl;
  cout << "Time elapsed: " << chrono::duration_cast<chrono::milliseconds>(end-begin).count() << " ms" << endl << endl;

  return 0;
}
