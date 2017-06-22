# Nama	: Hutama Tefotuho Hulu
# NIM	: 13515045

# File ini berisi kumpulan metode pengujian bilangan Prima
# Terdapat tiga buah metode yang dipakai disini yaitu
# Metode Miller-Rabin, metode Lucas, dan metode Jacobi Number
# Gabungan dari ketiga metode tersebut adalah metode Baillie-PSY
# yang terdapat pada akhir halaman ini

from math import sqrt

# Melakukan perkiraan bilangan prima dengan metode Miller-Rabin
def miller_rabin(n):
	d = n - 1
	s = 0
	while not d & 1:
		d = d >> 1
		s += 1
	
	x = pow(2, d, n)
	if x == 1 or x == n - 1:
		return True
	for i in range(s - 1):
		x = pow(x, 2, n)
		if x == 1:
			return False
		elif x == n - 1:
			return True
	return False

# Melakukan perkiraan bilangan prima dengan metode bilangan jacobi
def jacobi(a, n):
	if n == 1:
		return 1
	elif a == 0:
		return 0
	elif a == 1:
		return 1
	elif a == 2:
		if n % 8 in [3, 5]:
			return -1
		elif n % 8 in [1, 7]:
			return 1
	elif a < 0:
		return (-1)**((n-1)/2)*jacobi(-1*a,n)
	
	if a % 2 == 0:
		return jacobi(2,n) * jacobi(a / 2, n)
	elif a % n != a:
		return jacobi(a % n, n)
	else:
		if a % 4 == n % 4 == 3:
			return -1*jacobi(n, a)
		else:
			return jacobi(n, a)

# Fungsi ini digunakan untuk membantu perhitungan menggunakan tes Lukas
def UVsubscript(k, n, U, V, P, Q, D):
	k, n, U, V, P, Q, D = map(int, (k, n, U, V, P, Q, D))
	digits = list(map(int, str(bin(k))[2:]))
	subscript = 1
	for digit in digits[1:]:
		U, V = U*V % n, (pow(V, 2, n) - 2*pow(Q, subscript, n)) % n
		subscript *= 2
		if digit == 1:
			if not (P*U + V) & 1:
				if not (D*U + P*V) & 1:
					U, V = (P*U + V) >> 1, (D*U + P*V) >> 1
				else:
					U, V = (P*U + V) >> 1, (D*U + P*V + n) >> 1
			elif not (D*U + P*V) * 1:
				U, V = (P*U + V + n) >> 1, (D*U + P*V) >> 1
			else:
				U, V = (P*U + V + n) >> 1, (D*U + P*V + n) >> 1
			subscript += 1
			U, V = U % n, V % n
	return U,V

# Melakukan perkiraan bilangan prima dengan metode Lucas number
def lucas_pp (n, D, P, Q):
	U, V = UVsubscript(n + 1, n, 1, P, P, Q, D)
	
	if U != 0:
		return False
	
	d = n + 1
	s = 0
	
	while not d & 1:
		d = d >> 1
		s += 1
	
	U, V = UVsubscript(n + 1, n, 1, P, P, Q, D)
	
	if U == 0:
		return True
	
	for r in xrange(x):
		U, V = (U* V) % n, (pow(V, 2, n) - 2*pow(Q, d*(2**r), n)) % n
		if V == 0:
			return True
	
	return False

# Memilih sebuah basis yang akan digunakan untuk deteksi fungsi prima Baillie
def choose(kandidat):
	D = 5
	while jacobi(D, kandidat) != -1:
		D += 2 if D > 0 else -2
		D *= 1
	return D

# Melakukan deteksi bilangan prima dengan menggunakan metode Baillie
# Metode baillie adalah metode penentuan bilangan prima yang merupakan
# gabungan dari ketiga metode diatas, yaitu 
# metode Lucas, metode Jacobi Number, dan metode Miller Rabin
def baillie(kandidat):
	for bilprima in [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97]:
		if kandidat == bilprima:
			return True
		elif kandidat % bilprima == 0:
			return False
	
	if not miller_rabin(kandidat):
		return False
	
	if int(sqrt(kandidat) + 0.5) ** 2 == kandidat:
		return False
	
	if not lucas_pp(kandidat, choose(kandidat), 1, (1-choose(kandidat))/4):
		return False
	
	return True
