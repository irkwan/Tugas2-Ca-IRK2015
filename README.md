# Tugas2-Ca-IRK2015 RSA and Big Number Implementation 
## Big Number Implementation
Big Number merupakan salah satu *library* yang sangat berguna untuk melakukan kalkulasi diatas batas memory suatu bahasa pemrograman. Sudah banyak bahasa pemrograman yang menyediakan *library* tersebut salah satunya adalah Java, kemudian yang sudah bawaan untuk menghitung kalkulasi angka *BigNumber* seperti Python. Banyak sekali konsep yang dibawa pada implementasi pada Big Number. Konsep yang sering dipakai adalah *stack* dan *Dynamic Programming*. Salah satu penerapan *library* ini adalah pada RSA

## RSA
RSA di bidang kriptografi adalah sebuah algoritma pada enkripsi *__public key__*. RSA merupakan algoritma pertama yang cocok untuk *digital signature* seperti halnya ekripsi, dan salah satu yang paling maju dalam bidang kriptografi *public key*. RSA masih digunakan secara luas dalam protokol *electronic commerce*, dan dipercaya dalam mengamankan dengan menggunakan kunci yang cukup panjang.

Pada tugas kali ini, saya akan mengimplementasikan RSA dengan *library* Big Number yang sudah saya buat. Langkah proses RSA adalah sebagai berikut:

1. *Generate* dua buah bilangan prima *p* dan *q*;
2. Buatlah sebuah variabel baru, sebut saja *n*, dimana *n* = *p* x *q*;
3. Lalu, kita cari nilai λ(*n*) yang biasa disebut *Charmicael's totient function*, yang dimana nilai dari λ(*n*) = *LCM*(*p-1*,*q-1*);
4. Pilih sebuah bilangan, misal *e* dan *1* < *e* < λ(*n*) dan *GCD*(*e*,λ(*n*)) = *1*
5. Pilih sebuah bilangan *d* dimana *d* ≡ e⁻¹ (mod(λ(*n*)))

Disini *e* akan menjadi *public key exponent* dan *d* akan dijadikan *private key exponent*

## Running Program
1. Lakukan kompilasi program. Untuk *default command* yang saya gunakan adalah `g++ -o main "BigNumber/BigNumber.cpp" "RSA/RSA.cpp" main.cpp -std=c++11`.
2. *Run program* dengan melakukan *command* (pada Windows) `main` atau (pada Linux) `./main` di direktori dimana *main program* dihasilkan
3. Masukkan nama file yang akan dienkripsi, misal: `input.txt`. Kemudian masukkan nama file hasil menyimpan kode enkripsi, misal: `en.txt` dan hasil dekripsi misal: `de.txt`
4. Tunggu *running proses* dilakukan.

## Screenshot
![running](https://github.com/reiva5/Tugas2-Ca-IRK2015/blob/master/Jehian%20Norman%20Saviero%20-%2013515139/Screenshot/Running%20Process.png "Running Process")
![input](https://github.com/reiva5/Tugas2-Ca-IRK2015/blob/master/Jehian%20Norman%20Saviero%20-%2013515139/Screenshot/input.png "Input.txt")
![enkripsi](https://github.com/reiva5/Tugas2-Ca-IRK2015/blob/master/Jehian%20Norman%20Saviero%20-%2013515139/Screenshot/en.png "Hasil Enkripsi pada en.txt")
![dekripsi](https://github.com/reiva5/Tugas2-Ca-IRK2015/blob/master/Jehian%20Norman%20Saviero%20-%2013515139/Screenshot/de.png "Hasil Dekripsi pada de.txt")

## Referensi 
https://en.wikipedia.org/wiki/RSA_(cryptosystem)
