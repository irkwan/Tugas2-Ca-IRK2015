import random
import timeit
from BigPrime import *
from BigInt import BigInt

"""
  Euclid's algorithm to determine GCD
"""
def gcd(a, b):
  while b!=BigInt("0"):
    a, b = b, a % b
  return a

"""
  Finding Multiplicative inverse
"""
def multiplicative_inverse(e, phi):
  # e = int(e.num)
  # phi = int(phi.num)
  d = BigInt("0")
  a1 = BigInt("0")
  a2 = BigInt("1")
  b1 = BigInt("1")
  temp_phi = phi
  
  while e > BigInt("0"):
    # print d, a1, a2, b1, temp_phi
    temp1 = temp_phi/e
    temp2 = temp_phi-temp1*e
    temp_phi = e
    e = temp2
    
    a = a2-temp1*a1
    b = d-temp1*b1
    
    a2 = a1
    a1 = a
    d = b1
    b1 = b
  
  if temp_phi == BigInt("1"):
    return d + phi

"""
  Is Prime?
"""
def is_prime(num):
  if num == BigInt("2"):
    return True
  if num < BigInt("2") or num % BigInt("2") == BigInt("0"):
    return False
  n = BigInt("3")
  while(n<num):
    if num % n == BigInt("0"):
      return False
    n=n+BigInt("2")
  return True

def generate_keypair(p, q):
  # if not is_prime(p) or not is_prime(q):
  #   raise ValueError('Both must prime.')
  # elif p==q:
  #   raise ValueError('p, q cannot equal')

  # n=p*q
  n=p*q

  # Totient of n
  phi = (p-BigInt("1")) * (q-BigInt("1"))

  # Choose coprime integer
  e = BigInt(str("2"))

  # Verify e and phi are coprime
  g = gcd(e, phi)
  while g != BigInt("1"):
    e += BigInt("1")
    g = gcd(e, phi)
  
  # Generate private key
  d = multiplicative_inverse(e, phi)
    
  # Public key is (e, n) and private key is (d, n)
  return ((e, n), (d, n))

def encrypt(pub, plain):
  key, n = pub
  
  # a^b mod m
  cipher = []
  for char in plain:
    cipher+=[((BigInt(str(ord(char))) ** key) % n).num]
    # print cipher

  return cipher

def decrypt(priv, cipher):
  key, n = priv

  # a^b mod m
  plain = []
  # print key
  # print n
  for char in cipher:
    # print char, (char ** int(key.num)) % int(n.num)
    # plain+=[chr(int(((BigInt(char) ** key) % n).num))]
    # plain+=[chr(pow(int(char),int(key.num),int(n.num)))]
    a = long((BigInt(char).pow_mod(key,n)).num)
    # print a,
    plain+=[chr(a%255)]
  return ''.join(plain)
    

print "RSA"
p = str(generateLargePrime(64)) # 2^64-1 is not Big Integer :)
print "First prime number: ", p
q = str(generateLargePrime(64)) # 2^64-1 is not Big Integer :)
print "Second prime number: ", q
# p = str(raw_input("1 :"))
# q = str(raw_input("2 :"))
p = BigInt(p)
q = BigInt(q)
print

print "Generating public and private keypairs..."
start_time = timeit.default_timer()
public, private = generate_keypair(p, q)
elapsed_key = timeit.default_timer() - start_time
print "Public key : ", public[0], public[1]
print "Private key: ", private[0], private[1]
print "Generate key time: ", elapsed_key
print

print "Enter a message to encrypt: "
print "-----"
message = raw_input("")
print "-----"
print

print "Encrypting message with public key ",public[0], public[1],"..."
start_time = timeit.default_timer()
encrypted_msg = encrypt(public, message)
elapsed_enc = timeit.default_timer() - start_time
print "Encrypted messege:"
print "-----"
print ''.join(encrypted_msg)
print "-----"
print "Encrypting time: ", elapsed_enc
print
print "Total time generate + encrypt: ", elapsed_enc+elapsed_key
print
print "Decrypting message with private key ", private[0], private[1] ,"..."
print "Decrypted message:"
start_time = timeit.default_timer()
print "-----"
print decrypt(private, encrypted_msg)
print "-----"
elapsed_dec = timeit.default_timer() - start_time
print "Decrypting time: ", elapsed_dec