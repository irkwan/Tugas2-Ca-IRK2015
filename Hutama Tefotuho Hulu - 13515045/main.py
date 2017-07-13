# Nama	: Hutama Tefotuho Hulu
# NIM	: 13515045

# File ini berisi program utama dari aplikasi RSA. Berisi enkriptor,
# dekriptor, dan program utama.

from bignumber import randomprime, kali, kurang, primerelative, inversemod, moduloexponent
from converter import ReadFile, WriteFile, toASCII, toListASCII
import time

# Fungsi ini berfungsi untuk men-enkripsi tulisan pada file teks
def encrypt(p, e, n):
	c = []
	print("Hasil enkripsi : ")
	for i in range(len(p)):
		x = moduloexponent(int(p[i]),e,n)
		c.append(x)
	return c

# Fungsi ini berfungsi untuk men-dekripsi tulisan pada file teks
def decrypt(c, d, n):
	p = []
	print("Hasil dekripsi : ")
	for i in range(len(c)):
		x = moduloexponent(int(c[i]),d,n)
		p.append(x)
	return p

# Fungsi utama program
def main(inputfile, encryptfile, decryptfile):
	# Pengantar
	print("Program ini dapat melakukan enkripsi dari sebuah teks yang diberikan dan mengeluarkan dekripsi dari enkripsi teks dan mengembalikan teks menjadi seperti semula.")
	print '\n'
	
	# Memasukkan teks yang hendak dienkripsi ke dalam program
	content = ReadFile(inputfile)
	print("Teks yang dimasukkan adalah : ")
	print(content)
	plain = toListASCII(content)
	WriteFile("convertedtoascii.txt", plain)
	
	# Pencarian dua bilangan prima, kunci, hasil kali, dan toinent
	p = randomprime(50)
	q = randomprime(50)
	while (p == q):
		q = randomprime(40)
	n = kali(p, q)
	t = kali(kurang(p, 1), kurang(q, 1))
	e = primerelative(t)
	d = inversemod(e, n)
	
	start = time.time()
	
	# Enkripsi teks dan mencetak ke layar
	print '\n'	
	encrypttext = encrypt(plain, e, n)
	stringencrypt = ''
	for i in range(len(encrypttext)):
		stringencrypt = stringencrypt + str(encrypttext[i])
	print stringencrypt
	WriteFile(encryptfile, encrypttext)
	
	# Dekripsi teks dan mencetak ke layar
	print '\n'
	decrypttext = decrypt(encrypttext, d, n)
	stringdecrypt = ''
	for i in range(len(decrypttext)):
		stringdecrypt = stringdecrypt + str(decrypttext[i])
	print stringdecrypt
	WriteFile(decryptfile, stringdecrypt)
	
	end = time.time()
	
	# Informasi yang dibutuhkan, bilangan prima dan kuncinya
	print '\n'
	print ('Bilangan prima pertama adalah :')
	print p
	print '\n'
	print ('Bilangan prima kedua adalah :')
	print q
	print '\n'
	print ('Hasil kali kedua bilangan prima adalah :')
	print n
	print '\n'
	print ('Toinent kedua bilangan adalah :')
	print t
	print '\n'
	print ('Kunci enkripsi adalah :')
	print e
	print '\n'
	print ('Kunci dekripsi adalah :')
	print d
	print '\n'
	print ('Lama waktu eksekusi adalah :')
	print end - start

inputfile = "input.txt"
encryptfile = "encrypt.txt"
decryptfile = "decrypt.txt"
main(inputfile, encryptfile, decryptfile)
