# Nama	: Hutama Tefotuho Hulu
# NIM	: 13515045

# File ini berisi kumpulan fungsi yang berhubungan dengan file eksternal
# dari menulis file ke sebuah file eksternal, membaca file ke sebuah
# file eksternal, serta mengubah seluruh karakter menjadi ASCII sehingga
# dapat dienkripsi atau di dekripsi

# Fungsi ini berfungsi untuk membaca isi file eksternal
def ReadFile(filename) :
	file = open(filename, 'r')
	content = file.read()
	file.close()
	return content

# Fungsi ini berfungsi untuk menulis isi file eksternal
def WriteFile(filename, list) :
	file = open(filename, 'w')
	for i in list:
		file.write("%s\n" % i)

# Fungsi ini untuk merubah suatu konten file eksternal menjadi ASCII
def toASCII(word) :
	asc = ''.join(str(ord(c)) for c in word)
	return asc

# Fungsi ini mengubah suatu ASCII menjadi list of ASCII
def toListASCII(word) :
	list = []
	for c in word :
		list.append(ord(c))
	return list
