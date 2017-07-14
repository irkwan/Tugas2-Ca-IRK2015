# Tugas2-Ca-IRK2015 RSA and Big Number Implementation 


# Penjelasan Singkat

Aplikasi **RSA Encrypt/Decrypt** adalah sebuah aplikasi yang dapat melakukan proses enkripsi dan dekripsi data dalam bentuk teks maupun file menggunakan kriptosistem public-key **RSA** (Rivest Shamir Adleman).

Aplikasi ini dikembangkan menggunakan **bahasa pemrograman Java** dan berupa **aplikasi berbasis GUI** (**GUIMain**).

Aplikasi ini terdiri atas sebuah implementasi tipe data bilangan integer besar (**BigNumber**), implementasi tipe data key pair RSA (**RSAKeyPair**, yang terdiri atas **RSAPublicKey** dan **RSAPrivateKey**), dan sebuah implementasi tipe data padded message OAEP (**Message**).

Aplikasi ini memiliki tiga fungsi utama, yaitu: 
1. **Generasi key pair n-bit** (pasangan public key dan private key)
2. **Enkripsi dari plaintext menjadi ciphertext**
3. **Dekripsi dari ciphertext menjadi plaintext**

Tahap-tahap generasi key-pair n-bit yang digunakan oleh apliaksi ini adalah sebagai berikut:
1. Sebuah **bilangan random ganjil sebesar (n/2)-bit** dihasilkan menggunakan sebuah random number generator yang aman (SecureRandom dari java.security)
2. Bilangan tersebut dicek keprimaannya menggunakan **sieve trial division** dan **algoritma Miller-Rabin**
3. Jika bilangan tersebut bukan prima, maka bilangan tersebut ditambah dengan dua dan tahap 2 diulang kembali
4. Tahap 1 hingga 3 diulang menghasilkan bilangan prima random kedua
5. Dengan ``p`` adalah bilangan prima pertama dan ``q`` adalah bilangan prima kedua, nilai ``phi`` dihitung dengan rumus ``phi = (p - 1) * (q - 1)``
6. Nilai public exponent ``e`` dipilih dari range ``1 < e < phi``. Dalam implementasi ini, ``e = 65537`` seperti pada kebanyakan implementasi RSA lainnya. Nilai **e** dapat diganti oleh pengguna.
7. ``e`` juga harus coprime dengan ``phi``. Jika tidak maka tahap 1 hingga 6 diulang kembali
8. Nilai modulus ``n`` dihitung dengan rumus ``n = p * q`` menghasilkan **sebuah bilangan n-bit**
9. Nilai private exponent ``d`` dihitung dengan rumus ``d * e = 1 (mod n)`` (atau ``d = e.modularInverse(n)``)
10. Public key dihasilkan sebagai tuple ``(n, e)``
11. Private key dihasilkan sebagai tuple ``(n, d)``, atau sebagai quintuple ``(p, q, dP = e.modularInverse(p - 1), dQ = e.modularInverse(q - 1), qInv = q.modularInverse(p))``. Private key implementasi ini menyertakan kedua representasi dan juga public exponent ``e``.

Tahap-tahap proses enkripsi RSAES-OAEP yang digunakan oleh aplikasi ini adalah sebagai berikut:
1. **Data** diubah menjadi sebuah **Plaintext** berupa array of bytes dari **input file** (raw bytes) atau **input keyboard** (teks UTF-8)
2. **Plaintext** diubah menjadi sebuah **Encoded Message** menggunakan operasi **encoding padding EME-OAEP**. Kegunaan utama dari operasi padding ini adalah setiap operasi encoding dapat menghasilkan Encoded Message yg berbeda meskipun berasal dari Plaintext yang sama.
3. **Encoded Message** diubah menjadi nilai ``m`` menggunakan operasi konversi data **OS2IP**
4. Nilai ``m`` diubah menjadi nilai ``c`` menggunakan operasi primitif enkripsi **RSAEP** (``c = m ^ e (mod n)``)
5. Nilai ``c`` diubah menjadi **Ciphertext** menggunakan operasi konversi data **I2OSP**
6. **Ciphertext** diubah menjadi **Data** berupa array of bytes yang kemudian menjadi **output file** atau **output layar**

Tahap-tahap proses dekripsi RSAES-OAEP yang digunakan oleh aplikasi ini adalah sebagai berikut:
1. **Data** diubah menjadi sebuah **Ciphertext** berupa array of bytes dari **input file** (raw bytes)
2. **Ciphertext** diubah menjadi nilai ``c`` menggunakan operasi konversi data **OS2IP**
3. Nilai ``c`` diubah menjadi nilai ``m`` menggunakan operasi primitif dekripsi **RSADP** (``m = c ^ e (mod n)``)
4. Nilai ``m`` diubah menjadi sebuah **Encoded Message** menggunakan operasi konversi data **I2OSP**
5. **Encoded Message** diubah menjadi sebuah **Plaintext** menggunakan operasi **decoding padding EME-OAEP**. Operasi decoding menjamin kembalinya Plaintext yang asli dari Encoded Message yang berbeda-beda.
6. **Plaintext** diubah menjadi **Data** berupa array of bytes yang kemudian menjadi **output file** atau **output layar**

Detil mengenai operasi **OS2IP**, **I2OSP**, **RSAEP**, **RSADP**, **EME-OAEP encode**, dan **EME-OAEP decode** terdapat pada [dokumen standar PKCS#1 v2.2](https://www.emc.com/collateral/white-papers/h11300-pkcs-1v2-2-rsa-cryptography-standard-wp.pdf).

# Cara Penggunaan

1. Buka file aplikasi [rsa.jar](bin/rsa.jar)
2. Pilih tombol **Generate new key pair**
3. Pada dialog box yang terbuka, pilih nilai **Key length**, yaitu jumlah bit key yang diinginkan (minimum ``384``, step ``64``).
4. Hal di atas dapat disesuaikan dengan nilai **Message limit**, yaitu jumlah byte maksimum message yanga dapat dienkripsi.
5. Pilih tombol **Generate** untuk memulai proses generasi. Proses akan ditampilkan pada layar. Proses dapat dihentikan dengan tombol **Abort**. Sebuah message box dengan waktu eksekusi akan ditampilkan setelah proses selesai.
Prakiraan waktu kunci 512-bit adalah 3-5 detik, 1024-bit adalah 20-40 detik, 2014-bit adalah 3-4 menit.
6. Setelah proses selesai, pilih tombol **Load** untuk memasukkan data key pair ke aplikasi.
7. Jika sudah ada data key pair, maka tombol **View public/private key contents** (melihat isi key) dan **Save public/private key contents to file** (menyimpan key ke file eksternal) dapat digunakan jika diinginkan.
8. Tahap 1 hingga 7 dapat diganti dengan menggunakan tombol **Load public/private key from file** dan memilih file eksternal key yang telah digenerasi sebelumnya.
9. Pada text area **Message**, pengguna dapat melakukan input data dari keyboard. Message juga dapat diisi dengan menggunakan tombol **Load message** dan memilih file eksternal data yang ingin diinput. **Encoding** dapat diganti dari **UTF-8** menjadi **Hex**.
10. Pilih tombol **Encrypt message** untuk memulai proses enkripsi. Sebuah message box dengan waktu eksekusi akan ditampilkan setelah proses selesai. Hasil enkripsi akan ditampilkan pada layar.
11. Pengguna dapat menggunakan tombol **Save message** untuk menyimpan data hasil enkripsi ke sebuah file eksternal.
12. Pilih tombol **Decrypt message** untuk memulai proses dekripsi. Sebuah message box dengan waktu eksekusi akan ditampilkan setelah proses selesai. Hasil dekripsi akan ditampilkan pada layar.

# Screenshot Program

1. Tampilan awal program

| <img src="/Edwin%20Rachman%20-%2013515042/screenshots/0.png" height=252 /> |
| :-: |
| Program rsa.jar |

2. Tampilan awal generasi key pair


<img src="/Edwin%20Rachman%20-%2013515042/screenshots/512-0.png" height=252 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/1024-0.png" height=252 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/2048-0.png" height=252 />
:-: | :-: | :-:
512-bit | 1024-bit | 2048-bit

3. Waktu eksekusi generasi key pair

<img src="/Edwin%20Rachman%20-%2013515042/screenshots/512-1.png" height=252 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/1024-1.png" height=252 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/2048-1.png" height=252 />
:-: | :-: | :-:
512-bit | 1024-bit | 2048-bit

4. Tampilan awal message

<img src="/Edwin%20Rachman%20-%2013515042/screenshots/512-2.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/1024-2.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/2048-2.png" height=260 />
:-: | :-: | :-:
512-bit | 1024-bit | 2048-bit

5. Waktu eksekusi enkripsi message

<img src="/Edwin%20Rachman%20-%2013515042/screenshots/512-3.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/1024-3.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/2048-3.png" height=260 />
:-: | :-: | :-:
512-bit | 1024-bit | 2048-bit

6. Message hasil enkripsi

<img src="/Edwin%20Rachman%20-%2013515042/screenshots/512-4.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/1024-4.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/2048-4.png" height=260 />
:-: | :-: | :-:
512-bit | 1024-bit | 2048-bit

7. Waktu eksekusi dekripsi message

<img src="/Edwin%20Rachman%20-%2013515042/screenshots/512-5.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/1024-5.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/2048-5.png" height=260 />
:-: | :-: | :-:
512-bit | 1024-bit | 2048-bit

8. Message hasil dekripsi

<img src="/Edwin%20Rachman%20-%2013515042/screenshots/512-6.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/1024-6.png" height=260 /> | <img src="/Edwin%20Rachman%20-%2013515042/screenshots/2048-6.png" height=260 />
:-: | :-: | :-:
512-bit | 1024-bit | 2048-bit

# Referensi 

- [Artikel Wikipedia](https://en.wikipedia.org/wiki/RSA_(cryptosystem))

- [Dokumen standar PKCS#1 v2.2](https://www.emc.com/collateral/white-papers/h11300-pkcs-1v2-2-rsa-cryptography-standard-wp.pdf)

- [Spesifikasi tugas](Spesifikasi-Tugas.md/)