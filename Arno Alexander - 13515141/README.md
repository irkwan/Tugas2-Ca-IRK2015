# BigNumber dan RSA

## Tentang Library

Dalam repositori ini, terdapat library BigNumber dan RSA beserta contoh program demo enkripsi dan dekripsi file teks. Library dan program dibuat menggunakan bahasa C++.

BigNumber menyimpan digit-digit bilangan dalam sebuah array dengan struktur little endian. Basis yang digunakan adalah 1000000000. Operator-operator yang biasa digunakan untuk tipe primitif integer juga dapat diterapkan pada BigNumber. Kemampuan lainnya yaitu membangkitkan bilangan secara acak, membangkitkan bilangan mungkin prima dengan menerapkan Fermat primality test, serta menghitung lcm dan gcd menggunakan algoritma Euclid.

RSA membutuhkan dua buah bilangan prima untuk membangkitkan kunci enkripsi (n,e) dan dekripsi (n,d). Misalkan bilangan prima tersebut p dan q. n didapatkan dengan mengalikan kedua bilangan prima tersebut. Selanjutnya, buat sebuah bilangan m=lcm(p-1,q-1). Kemudian cari d dan e sedemikian sehingga e relatif prima terhadap m dan d.e = 1 mod m. Sebuah karakter a dapat dienkripsi menjadi c dengan cara c = a<sup>e</sup> (mod n), sebaliknya c dapat didekripsi kembali menjadi a dengan cara a = c<sup>d</sup> mod n. Namun, cara tersebut kurang aman karena masing-masing karakter hanya memiliki satu kemungkinan hasil enkripsi. Untuk itu, jika a adalah karakter extended ASCII, kode ASCII a ditambahkan dengan bilangan acak kelipatan 256 pada tahap enkripsi dan dimodulo 256 pada tahap dekripsi. Dengan demikian, sebuah teks yang sama dapat menghasilkan banyak kemungkinan ciphertext.

## Cara Penggunaan

**Kompilasi:**
1. Buka direktori source code di terminal
2. Masukkan command `g++ *.cpp -o program`

**Menjalankan program:**
1. Masukkan path untuk file yang ingin dienkripsi, file hasil enkripsi, dan file hasil dekripsi
2. Tunggu hingga proses selesai

## Screenshot

TBD