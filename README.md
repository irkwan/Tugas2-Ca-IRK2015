# Tugas2-Ca-IRK2015 RSA and Big Number Implementation 

# Penjelasan Singkat

Program kriptografi dengan algoritma RSA ini saya buat dengan menggunakan bahasa C++.
Ketika program dijalankan, program akan langsung melakukan generate 2 bilangan prima dengan panjang 20 digit dan 25 digit.
Lalu, program akan menghitung n serta menghitung Euler's Totient Function dari n, baru setelah itu melakukan generate bilangan random untuk mencari suatu bilangan yang relatif prima
dengan Euler's Totient Function dari n beserta modular inverse nya yang kemudian menjadi e dan d.
Setelah itu, program meminta masukan dari user apa yang ingin dilakukan. Program dapat melakukan 2 hal, yaitu mengenkripsi suatu file atau melakukan dekripsi terhadap suatu file
yang sudah pernah dienkripsi sebelumnya.

# Cara Penggunaan

Ketika program sudah siap, program akan menampilkan interface sebagai berikut :
Menu :
1. Encrypt Files
2. Decrypt Files
3. Exit

Input :

Masukkan angka 1-3 sesuai dengan yang diinginkan. 1 untuk enkripsi, 2 untuk dekripsi, dan 3 untuk keluar dari program.
Setelah memasukkan angka 1 atau 2, program akan meminta masukan nama file yang ingin dienkripsi/didekripsi.
Masukkan nama file, lalu tunggu beberapa saat sampai program menyelesaikan proses tersebut.
Setelah selesai, program akan kembali menampilkan menu utama, program hanya akan keluar jika user memasukkan angka 3 pada menu utama.

# Contoh Hasil Eksekusi Program

- Contoh 1
Input : 1

Enter file's name : test.txt
Please wait until the process is done.

Ciphertext :
☺S♀e↓♥aA§)A⌂BoVWL|v

Encryption time : 0.703

- Contoh 2
Input : 2

Enter file's name : test.txt
Please wait until the process is done.

Plaintext :
IRK


Decryption time : 2.283
