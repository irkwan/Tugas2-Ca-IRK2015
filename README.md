# RSA

### Penjelasan singkat

1. RSA menggunakan 2 bilangan prima (dalam program ini menggunakan bilangan prima sebesar 64bit yang otomatis tergenerate)
2. Hitung nilai n = p*q dan phi = (p-1)*(q-1)
3. Public key e, merupakan angka yang relatif prima dengan phi
4. Private key d, merupakan angka yang diperoleh menggunakan multiplicative inverse e dan phi
5. Text dienkripsi dengan public key, dan akan didekripsi dengan private key
6. Enkripsi karakter m adalah c = m^e%n

Program yang saya buat memilih public key yang paling minimum, sehingga enkripsi menjadi cepat. Namun, private key yang dipilih merupakan angka yang besar, sehingga dekripsi menjadi lebih lama karena algoritma pow_mod pada BigInt yang saya implementasikan belum optimal

Implementasi BigInt mengunakan manipulasi string. Untuk operator +,- dilakukan dengan menghitung satu persatu karakter secara bersusun. Operator / menggunakan algoritma long division, sedangkan operator * menggunakan algoritma karatsuba. Operator ^ menggunakan manipulasi bit.

### Installation
Masuk kedalam folder "Dery - 13515097"
```sh
$ python RSA.py
```
Pastikan versi python2.7

### Screenshot