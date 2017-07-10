# BigNumber dan RSA

## Tentang Library

Dalam repositori ini, terdapat library BigNumber dan RSA beserta contoh program demo enkripsi dan dekripsi file teks. Library dan program dibuat menggunakan bahasa C++.

BigNumber menyimpan digit-digit bilangan dalam sebuah array dengan struktur little endian. Basis yang digunakan adalah 1000000000. Operator-operator yang biasa digunakan untuk tipe primitif integer juga dapat diterapkan pada BigNumber. Kemampuan lainnya yaitu membangkitkan bilangan secara acak, membangkitkan bilangan mungkin prima dengan menerapkan Fermat primality test, serta menghitung lcm dan gcd menggunakan algoritma Euclid.

RSA membutuhkan dua buah bilangan prima untuk membangkitkan kunci enkripsi (n,e) dan dekripsi (n,d). Misalkan bilangan prima tersebut p dan q. n didapatkan dengan mengalikan kedua bilangan prima tersebut. Selanjutnya, buat sebuah bilangan m=lcm(p-1,q-1). Kemudian cari d dan e sedemikian sehingga e relatif prima terhadap m dan d.e = 1 mod m. Sebuah karakter a dapat dienkripsi menjadi c dengan cara c = a<sup>e</sup> (mod n), sebaliknya c dapat didekripsi kembali menjadi a dengan cara a = c<sup>d</sup> mod n. Namun, cara tersebut kurang aman karena masing-masing karakter hanya memiliki satu kemungkinan hasil enkripsi. Untuk itu, jika a adalah karakter extended ASCII, kode ASCII a ditambahkan dengan bilangan acak kelipatan 256 pada tahap enkripsi dan dimodulo 256 pada tahap dekripsi. Dengan demikian, sebuah teks yang sama dapat menghasilkan banyak kemungkinan ciphertext.

## Cara Penggunaan

**Kompilasi:**
1. Pastikan compiler C++ telah terpasang. Disarankan untuk menggunakan compiler dari [MinGW](http://www.mingw.org/ "Minimalist GNU for Windows")
2. Buka direktori source code di terminal
3. Masukkan command `g++ *.cpp -o program`

**Menjalankan program:**
1. Eksekusi program
2. Masukkan path untuk file yang ingin dienkripsi, file hasil enkripsi, dan file hasil dekripsi
3. Tunggu hingga proses selesai

## Screenshot

![plaintext](https://github.com/arnoalexander/Tugas2-Ca-IRK2015/blob/master/Arno%20Alexander%20-%2013515141/Screenshot/plain.JPG "Plaintext")
![ciphertext](https://github.com/arnoalexander/Tugas2-Ca-IRK2015/blob/master/Arno%20Alexander%20-%2013515141/Screenshot/encrypt.JPG "Ciphertext hasil enkripsi plaintext")
![plaintext2](https://github.com/arnoalexander/Tugas2-Ca-IRK2015/blob/master/Arno%20Alexander%20-%2013515141/Screenshot/decrypt.JPG "Plaintext hasil dekripsi ciphertext")
![program](https://github.com/arnoalexander/Tugas2-Ca-IRK2015/blob/master/Arno%20Alexander%20-%2013515141/Screenshot/program.JPG "Tampilan program saat dijalankan")