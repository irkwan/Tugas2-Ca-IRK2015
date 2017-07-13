# RSA dan Big Number Implementation
## Penjelasan Singkat
Library dari BigInteger beserta RSA di implementasi menggunakan bahasa C++. Dipilih bahasa C++ karena lebih mudah dalam melakukan implementasi khususnya untuk operator overloading. Selain itu, diterapkan pula gcdextended dan gcd yang berdasarkan algoritma Euclidian. Terdapat pula method generateprime untuk menghasilkan angka prima secara random. Angka-angka prima yang sudah di generate secara random akan menjadi variabel p dan q yang akan digunakan pada RSA. Kunci publik adalah pasangan (n,e) sedangkan kunci privat adalah pasangan (n, d). n didapatkan dengan cara mengalikan p dengan q. Totient didapatkan dengan cara mengalikan (p-1) dengan (q-1). Kemudian pilih suatu bilangan e yang relatif prima terhadap totient. d dapat dihitung dengan rumus d kongruen e-1 mod (totient). Selanjutnya teks yang diinput pada file eksternal akan di enkripsi. Ukuran kunci untuk enkripsi dapat dibedakan menjadi 512, 1024, dan 2048 bit. Semakin besar ukuran bit maka semakin rumit kunci tersebut untuk diretas oleh orang lain.

## Cara Penggunaan
**Cara Kompilasi Program:**
1. Buka terminal kemudian masuk ke direktori source code yang dituju (Kezia Suhendra - 13515063).
2. Ketik command `g++ -o main main.cpp BigInteger.cpp Key.cpp OAEP.cpp PrimeGenerator.cpp RSA.cpp StringTrans.cpp -std=c++11` untuk mengkompilasi program.

**Cara Menjalankan Program:**
1. Ketik command `./main ukuran namafiletxt`. Contohnya `./main 512 input1.txt`.
2. Angka 512 pada contoh di poin 1 diatas dapat diubah menjadi 1024 ataupun 2048 tergantung ukuran bit pada key yang diinginkan. Semakin besar ukuran key, akan semakin sulit untuk dipecahkan.

## Screenshot
![fileinput] [1]
![hasilfile1] [2]
![hasilfile2] [3]
![hasilfile3] [4]

[1]: Kezia\ Suhendra\ -\ 13515063/screenshot/Screen\ Shot\ (file\ input).png
[2]: Kezia\ Suhendra\ -\ 13515063/screenshot/Screen\ Shot\ (input1.txt).png
[3]: Kezia\ Suhendra\ -\ 13515063/screenshot/Screen\ Shot\ (input2.txt).png
[4]: Kezia\ Suhendra\ -\ 13515063/screenshot/Screen\ Shot\ (input3.txt).png
