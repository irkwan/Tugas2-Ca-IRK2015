# Nama	: Hutama Tefotuho Hulu
# NIM	: 13515045

# File ini berisi pustaka yang mengatur big number pada Python
# Fungsi-fungsi biasa pada awalnya ingin dituliskan sebagai algoritma
# big number, namun ternyata Python dapat mengatasi permasalahn tersebut

import numbers
import random
from baillie import baillie

# Fungsi pertambahan biasa
def tambah(a, b):
	return a + b

# Fungsi pengurangan biasa
def kurang(a, b):
	return a - b

# Fungsi perkalian biasa
def kali(a, b) :
	return a * b

# Fungsi permbagian biasa
def bagi(a,b) :
	return a / b

# Menentukan Faktor Persekutuan Terbesar dari dua buah bilangan
def fpb(a, b) :
	if(a < b):
		temp = a
		a = b
		b = temp
	while(a % b != 0):
		temp = a
		a = b
		b = temp%b
	return b

# Menentukan Kelipatan Persekutuan Terkecil dari dua buah bilangan
def kpk(a, b) :
	return bagi(kali(a, b), fpb(a, b))

# Fungsi ini menentukan suatu bilangan yang relatif prima terhadap parameter bilangan
# yang diberikan. Program ini mencari suatu bilangan yang relatif prima
# dimulai dari bilangan kuadrat angka yang diberikan
def primerelative(a):
	x = a
	while(fpb(x,a) != 1):
		x = x + 1
	return x

# Fungsi ini digunakan untuk melakukan penyelesaian terhadap persamaan
# ax + by = c, dimana a dan b diberikan, dan kita harus mencari x dan y
# yang merupakan modifikasi dari Faktor Persekutuan Terbesar
def extendedfpb(a, b):
    if (a == 0):
        return (b, 0, 1)
    else:
        g, x, y = extendedfpb(b%a, a)
        return (g, y-(b//a)*x, x)

# Fungsi ini mencari bilangan yang merupakan invers modular dari parameter 
# yang diberikan e*d = 1 (mod m)
def inversemod(e, m):
	g, x, y = extendedfpb(e, m)
	return x % m

# Fungsi ini melakukan pencarian sisa hasil bagi suatu bilangan dengan
# persamaan a^b = x(mod m)
def moduloexponent(a, b, m) :
	p = 1
	a1 = a % m
	while(b > 0):
		if (b % 2 == 1):
			p = p * a1
			p = p % m
		b = b // 2
		a1 = (a1 * a1) % m
	return p

# Fungsi ini mengeluarkan suatu bilangan random yang merupakan prima
# Fungsi ini merupakan modifikasi dari fungsi random number
def randomprime(digit):
	down = 10**(digit-1)
	up = 10**(digit)-1
	p = random.randint(down, up)
	if baillie(p):
		return p
	else:
		return randomprime(digit)
