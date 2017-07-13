# Tugas2-Ca-IRK2015 RSA and Big Number Implementation 

## Penjelasan Singkat

Pada tugas kali ini, Ca-IRK 2015 mendapat tugas untuk mengimplementasikan algoritma dalam bidang kriptografi yang cukup terkenal.
Algoritma tersebut adalah algoritma RSA. RSA termasuk kedalam algoritma kriptografi non-simetris, RSA banyak dipakai karena tingkat 
keamanannya yang cukup baik dengan memanfaatkan prinsip operasi pada big number.
Selain itu, Ca-IRK 2015 juga harus mengimplementasikan library big number hasil buatan sendiri.

Implementasi library big number terdapat pada file [BigInteger.java](https://github.com/calmira/Tugas2-Ca-IRK2015/blob/master/Catherine%20Almira%20-%2013515111/BigInteger.java).

Di dalam file tersebut, big number dibuat dengan memiliki atribut sign (penanda positif, negatif, atau 0) dan value (nilai dari big number itu sendiri).
Operasi dasar (penjumlahan, pengurangan, perkalian, pembagian, dan mod), gcd (greatest common divisor/faktor pembagi bersama terbesar), invers dari mod, dan pembangkitan bilangan prima secara acak dengan menggunakan Miller Rabin Test diimplementasikan pada file ini.

Implementasi algoritma RSA terdapat pada file [RSAGenerator.java](https://github.com/calmira/Tugas2-Ca-IRK2015/blob/master/Catherine%20Almira%20-%2013515111/RSAGenerator.java).

Untuk menerapkan algoritma RSA, pertama-tama dipilih 2 buah bilangan prima secara acak, misal p dan q.
Kemudian dihitung nilai dari n = p * q dan phi = (p - 1) * (q - 1).
Pilih kunci publik, e, yang relatif prima dengan phi.
Kunci privat, d, akan diperoleh dari invers e dalam modulus phi.
Hasil enkripsi terhadap m diperoleh dengan c = m<sup>e</sup> mod n, sementara hasil dekripsi terhadap c diperoleh dengan m = c<sup>d</sup> mod n.
Pada RSAGenerator ini, enkripsi dilakukan per karakter agar tidak mengubah makna dari karakter tersebut.
Untuk meningkatkan keamanan, enkripsi per karakter ini dilakukan dengan menjumlahkan suatu bilangan acak dikali dengan 256 terhadap nilai dari karakter tersebut agar dari karakter yang sama diperoleh hasil enkripsi yang berbeda. Hasil enkripsi yang akan didekripsi kembali dimod dengan 256 agar menghasilkan karakter sesuai dengan asalnya.
Selain mengimplementasikan enkripsi dan dekripsi dengan algoritma RSA, file tersebut pun dilengkapi dengan konversi teks menjadi big number dan sebaliknya.
Konversi dilakukan untuk memudahkan proses enkripsi dan dekripsi.

## Cara Penggunaan dan Screenshot Aplikasi

1. Download repository ini.
2. Buka direktori Catherine Almira - 13515111 di terminal.
3. Masukan perintah `javac Main.java` kemudian `java Main`.
4. Aplikasi seperti berikut akan muncul pada layar.
![Main view](https://raw.githubusercontent.com/calmira/Tugas2-Ca-IRK2015/master/Catherine%20Almira%20-%2013515111/Screenshot/MainView.PNG)

