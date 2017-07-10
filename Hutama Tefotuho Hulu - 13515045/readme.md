# Deskripsi Program

Program RSA berikut adalah program yang dapat melakukan kriptografi RSA. Terdapat beberapa kasus yang harus diselesaikan apabila kita hendak melakukan kriptografi dengan metode ini.

# Pustaka Big Number

Kasus pertama yang harus diselesaikan adalah pustaka Big Number. Dalam algoritma RSA, terdapat berbagai jenis metode yang harus diselesaikan dengan menggunakan Big Number, yaitu 
- Perkalian dua bilangan untuk menentukan n dan toinent
- Melakukan pencarian invers modulo untuk menentukan public dan private key
- Melakukan modulo pangkat untuk menemukan angka baru yang dikehendaki

Ketiga permasalahan diatas dapat diselesaikan dengan mudah dengan menggunakan Bahasa Python, yang juga merupakan alasan mengapa penulis menggunakan bahasa ini. Bahasa Python memiliki range digit yang besar. Sehingga, pustaka big number yang digunakan penulis dalam program ini hanya berupa perkalian biasa. Selain itu, fungsi modulo bilangan berpangkat dan mencari modulo bilangan yang sangat besar juga dapat dicari dengan menggunakan bahasa Python, sehingga kita tidak perlu membuat fungsi/pustaka yang baru lagi.

# Deteksi Bilangan Prima

Kasus kedua adalah melakukan deteksi bilangan Prima. Dalam hal ini, penulis menggunakan metode pengujian bilangan prima metode Baillie-PSW. Secara singkat, metode Baillie-PSW adalah sebagai berikut :
- Menguji apakah suatu bilangan dapat dibagi dengan bilangan prima yang cukup kecil. Dalam hal ini, penulis menguji sampai dengan bilangan prima dibawah 100
- Melakukan pengujian dengan menggunakan Strong Probable Number
- Menentukan dengan menggunakan Jacobi Number
- Melakukan pengujian Lucas

Apabila suatu bilangan telah lulus keempat hal tersebut, bisa diasumsikan bahwa bilangan tersebut adalah bilangan prima. Mengingat bahwa bilangan yang harus dipakai adalah bilangan prima diatas 20 digit, maka keempat tes ini harus dilakukan agar kita semakin yakin mendapatkan bilangan prima

# Proses Enkripsi dan Dekripsi

Kasus ketiga adalah melakukan enkripsi dan dekripsi. Caranya adalah dengan mengkonversi sebuah teks ke ASCII. Kemudian, hasil konversi itu dikelompokkan berdasarkan huruf, dan kemudian di-"putar" oleh kunci enkripsi.

# Program Utama

Berikut ini adalah mekanisme kerja dari program utama
- Pertama, dimasukkan sebuah teks ke dalam program. 
- Setelah masuk, program mengeluarkan dua bilangan. Kedua bilangan ini dikalikan untuk mendapatkan nilai n.
- Kedua bilangan kemudian dikurangi 1, kemudian dikalikan untuk mendapatkan nilai toinent
- Program kemudian mengeluarkan sebuah bilangan yang relatif prima dengan t, bilangan ini adalah kunci publiknya.
- Kemudian, kunci publik akan dicari invers modulonya dengan t, bilangan ini adalah kunci privat.
- Teks yang sudah dimasukkan akan dikonversi menjadi ASCII, kemudian dicari bilangan penggantinya dengan cara mempangkatkan ASCII dengan kunci publik modulo n. Seluruh perubahan teks adalah teks yang sudah dienkripsi.
- Teks yang sudah dienkripsi dapat dikonversi balik dengan cara memangkatkan bilangan dengan kunci privatnya, dengan modulo n. Teks ini adalah teks yang sama dengan awal tadi.

# Simulasi

Catatan : Fungsi simulasi ini hanya diberikan sedikit digit, dikarenakan sulit memberi contoh untuk 20 digit bilangan. Contoh lengkap dapat dilihat dalam dokumentasi program.

Teks : ganteng<br>
Bil1 : 11<br>
Bil2 : 13<br>
n : 143<br>
t : 120<br>
Kunci Publik : 7<br>
Kunci Privat : 103<br>

ganteng -> 103 097 110 116 101 110 103

Proses Enkripsi<br>
103^7 mod 143 = 038<br>
097^7 mod 143 = 059<br>
110^7 mod 143 = 033<br>
116^7 mod 143 = 129<br>
101^7 mod 143 = 062<br>

Teks hasil enkripsi -> 038 059 033 129 062 033 038

Proses Dekripsi<br>
038^7 mod 103 = 103<br>
059^7 mod 103 = 097<br>
033^7 mod 103 = 110<br>
129^7 mod 103 = 116<br>
062^7 mod 103 = 101<br>

Teks hasil dekripsi -> 103 097 110 116 101 110 103
