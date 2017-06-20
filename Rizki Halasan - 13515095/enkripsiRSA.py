import random
import time
from math import sqrt

def BacaFile(namafile) :
    file = open(namafile, 'r')
    isifile = file.read()
    file.close()
    return isifile

def TulisFile(namafile, list):
    file = open(namafile, 'w')
    for i in list:
        file.write("%s" % i)

def KatatoASCII(kata) :
    asc = ''.join(str(ord(c)) for c in kata)
    return asc

def KatatoListASCII(kata) :
    list = []
    for c in kata:
        list.append(ord(c))
    return list

def ASCIItoKata(asc):
    kata = ''.join(chr(num) for num in asc)
    return kata

def generate_random_number(digit):
    bawah = 10**(digit-1)
    atas = 10**(digit)-1
    return random.randint(bawah, atas)

def jacobi_symbol(a, n):
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
        return (-1)**((n-1)/2) * jacobi_symbol(-1*a, n)

    if a % 2 == 0:
        return jacobi_symbol(2, n) * jacobi_symbol(a / 2, n)
    elif a % n != a:
        return jacobi_symbol(a % n, n)
    else:
        if a % 4 == n % 4 == 3:
            return -1 * jacobi_symbol(n, a)
        else:
            return jacobi_symbol(n, a)

def U_V_subscript(k, n, U, V, P, Q, D):
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
            elif not (D*U + P*V) & 1:
                U, V = (P*U + V + n) >> 1, (D*U + P*V) >> 1
            else:
                U, V = (P*U + V + n) >> 1, (D*U + P*V + n) >> 1
            subscript += 1
            U, V = U % n, V % n
    return U, V

def lucas_pp(n, D, P, Q):
    U, V = U_V_subscript(n+1, n, 1, P, P, Q, D)

    if U != 0:
        return False
    d = n + 1
    s = 0
    while not d & 1:
        d = d >> 1
        s += 1
    U, V = U_V_subscript(n+1, n, 1, P, P, Q, D)
    if U == 0:
        return True
    for r in xrange(s):
        U, V = (U*V) % n, (pow(V, 2, n) - 2*pow(Q, d*(2**r), n)) % n
        if V == 0:
            return True
    return False

def miller_rabin_base_2(n):
    d = n-1
    s = 0
    while not d & 1:
        d = d >> 1
        s += 1
    x = pow(2, d, n)
    if x == 1 or x == n-1:
        return True
    for i in range(s-1):
        x = pow(x, 2, n)
        if x == 1:
            return False
        elif x == n - 1:
            return True
    return False

def D_chooser(candidate):
    D = 5
    while jacobi_symbol(D, candidate) != -1:
        D += 2 if D > 0 else -2
        D *= -1
    return D

def baillie_test(candidate):
    for known_prime in [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47]:
        if candidate == known_prime:
            return True
        elif candidate % known_prime == 0:
            return False
    if not miller_rabin_base_2(candidate):
        return False
    if int(sqrt(candidate) + 0.5) ** 2 == candidate:
        return False
    D = D_chooser(candidate)
    if not lucas_pp(candidate, D, 1, (1-D)/4):
        return False
    return True

def generate_random_prime(digit):
    p = generate_random_number(digit)
    if baillie_test(p):
        return p
    else:
        return generate_random_prime(digit)

def gcd(a, b):
    if(a < b):
        temp = a
        a = b
        b = temp
    while(a%b != 0):
        temp = a
        a = b
        b = temp%b
    return b

def relatif_prima(a):
    x = 2
    while(gcd(x, a) != 1):
        x = x+1
    return x

def extended_gcd(a, b):
    if (a == 1):
        return (1, 1, 0)
    else:
        g, y, x = extended_gcd(b%a, a)
        return (g, x-(b//a)*y, y)

def invers_modulo(e, m):
    g, x, y = extended_gcd(e, m)
    return x%m

def hitung_kunci_dekripsi(e, m):
    return invers_modulo(e, m)

def enkripsi(p, e, n):
    c = []
    for i in range(len(p)):
        x = pow(int(p[i]),e,n)
        c.append(x)
    return c

def dekripsi(c, d, n):
    p = []
    for i in range(len(c)):
        x = pow(int(c[i]),d,n)
        p.append(x)
    return p

def RSA(file_input, file_enkripsi, file_dekripsi):
    isifile = BacaFile(file_input)
    print("Teks yang akan didekripsi adalah : ", isifile)
    plaintext = KatatoListASCII(isifile)
    TulisFile('asciiplaintext.txt', plaintext)
    start_time = time.time()
    x = generate_random_prime(22)
    y = generate_random_prime(22)
    print("bilangan prima 1 (x) : ", x)
    print("bilangan prima 2 (y) : ", y)
    n = x * y
    print("x kali y = ", n)
    m = (x-1)*(y-1)
    print("x-1 dikali y-1 (m) = ", m)
    e = relatif_prima(m)
    print("e : ", e)
    d = hitung_kunci_dekripsi(e, m)
    print("kunci dekripsi (d) : ", d)
    ciphertext = enkripsi(plaintext, e, n)
    print("ciphertext :")
    for i in range(len(ciphertext)):
        print(ciphertext[i])
    TulisFile(file_enkripsi, ciphertext)
    waktu_eksekusi = time.time()-start_time
    print("Berhasil memasukkan ciphertext ke file ", file_enkripsi)
    dekripsiasc = dekripsi(ciphertext, d, n)
    decryptext = ASCIItoKata(dekripsiasc)
    print('Hasil dekripsi dari ciphertext tersebut :')
    print(decryptext)
    TulisFile(file_dekripsi, decryptext)
    print("Berhasil memasukkan hasil dekripsi ke file : ", file_dekripsi)
    print("Waktu eksekusi : ", waktu_eksekusi," detik")


file_input = "input.txt"
file_enkripsi = "enkripsi.txt"
file_dekripsi = "dekripsi.txt"
RSA(file_input, file_enkripsi, file_dekripsi)