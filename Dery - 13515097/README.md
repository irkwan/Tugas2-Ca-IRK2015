# RSA

## Penjelasan singkat

1. RSA menggunakan 2 bilangan prima (dalam program ini menggunakan bilangan prima sebesar 64bit yang otomatis tergenerate)
2. Hitung nilai n = p\*q dan phi = (p-1)\*(q-1)
3. Public key e, merupakan angka yang relatif prima dengan phi
4. Private key d, merupakan angka yang diperoleh menggunakan multiplicative inverse e dan phi
5. Text dienkripsi dengan public key, dan akan didekripsi dengan private key
6. Enkripsi karakter m adalah c = m^e%n

Program yang saya buat memilih public key yang paling minimum, sehingga enkripsi menjadi cepat. Namun, private key yang dipilih merupakan angka yang besar, sehingga dekripsi menjadi lebih lama karena algoritma pow_mod pada BigInt yang saya implementasikan belum optimal

Implementasi BigInt mengunakan manipulasi string. Untuk operator +,- dilakukan dengan menghitung satu persatu karakter secara bersusun. Operator / menggunakan algoritma long division, sedangkan operator * menggunakan algoritma karatsuba. Operator ^ menggunakan manipulasi bit.

## Cara Penggunaan

Pastikan versi python yang digunakan adalah 2.7

1. Edit kata yang ingin dienkripsi pada file msg.in
2. Masuk kedalam terminal
3. Jalankan program
```sh
$ python RSA.py
```
4. Program akan menuliskan output cipher text kedalam cipher_msg.ci
5. Program akan menuliskan hasil dekripsi pada layar yang diambil dari cipher_msg.ci

## Screenshot

![Screenshot Message](https://github.com/deryrahman/Tugas2-Ca-IRK2015/blob/master/Dery%20-%2013515097/msg.png)
![Screenshot Cipher](https://github.com/deryrahman/Tugas2-Ca-IRK2015/blob/master/Dery%20-%2013515097/cipher.png)
![Screenshot Terminal](https://github.com/deryrahman/Tugas2-Ca-IRK2015/blob/master/Dery%20-%2013515097/screenshot.png)
