"""
  Name : Dery Rahman Ahaddienata
  Nim  : 13515097
  
  RSA Generator.
  Read from file external (msg.in), and encrypt to file external (cipher_msg.ci)
"""
import random
import timeit
from BigPrime import *
from BigInt import BigInt

"""
  For coloring on terminal
"""
class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'

"""
  Euclid's algorithm to determine GCD
"""
def gcd(a, b):
  while b!=BigInt("0"):
    a, b = b, a % b
  return a

"""
  Multiplicative inverse algorithm to find private key
"""
def multiplicative_inverse(e, phi):
  d = BigInt("0")
  a1 = BigInt("0")
  a2 = BigInt("1")
  b1 = BigInt("1")
  temp_phi = phi
  
  while e > BigInt("0"):
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
  Generate public key and private key, from 2 prime number
"""
def generate_keypair(p, q):
  # n=p*q
  n=p*q

  # Totient of n
  phi = (p-BigInt("1")) * (q-BigInt("1"))

  # Choose coprime integer
  e = BigInt("2")

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
  
  # c = m^e mod n
  cipher = []
  for char in plain:
    cipher+=[((BigInt(str(ord(char))) ** key) % n).num]

  return cipher

def decrypt(priv, cipher):
  key, n = priv

  # m = c^d mod n
  plain = []
  for char in cipher:
    a = long((BigInt(char).pow_mod(key,n)).num)
    plain+=[chr(a%256)]

  return ''.join(plain)

if __name__ == "__main__":
  print "RSA"
  print
  print bcolors.BOLD, "Message from file external:", bcolors.ENDC

  # Read file external
  f = file('msg.in', 'r')
  message=""
  for line in f:
    message += line
  f.close()
  print
  print message
  for i in range((len(message)/2+1)%20):
    print "-",
    if i>20: break
  print
  print

  # Generate large prime
  p = str(generate_large_prime(64)) # 2^64-1 is not Big Integer :)
  print "First prime number : ", p
  q = str(generate_large_prime(64)) # 2^64-1 is not Big Integer :)
  print "Second prime number: ", q

  p = BigInt(p)
  q = BigInt(q)
  print

  # Generating public and private key
  print "Generating public and private keypairs..."
  start_time = timeit.default_timer()
  public, private = generate_keypair(p, q)
  print bcolors.OKGREEN, bcolors.BOLD, "Public key : ", bcolors.ENDC
  print "   e =", bcolors.OKGREEN, public[0], bcolors.ENDC
  print "   n =", bcolors.OKGREEN, public[1], bcolors.ENDC
  print bcolors.OKBLUE, bcolors.BOLD, "Private key: ", bcolors.ENDC
  print "   d =", bcolors.OKBLUE, private[0], bcolors.ENDC
  print "   n =", bcolors.OKBLUE, private[1], bcolors.ENDC
  print

  # Encrypt messege
  print bcolors.BOLD, "Encrypted messege:", bcolors.ENDC
  print "Encrypting message with public key ", bcolors.OKGREEN ,public[0], public[1],"...",bcolors.ENDC
  encrypted_msg = encrypt(public, message)
  elapsed = timeit.default_timer() - start_time
  print
  en_msg = ' '.join(encrypted_msg)
  print en_msg

  # Output cipher text to external file
  f = open("cipher_msg.ci","w")
  for i in encrypted_msg:
    print >> f, i,
  f.close()

  # Write it into terminal
  for i in range((len(en_msg)/2+1)):
    print "-",
    if i>20: break
  print
  print "Generate key + encrypting time: ", elapsed
  print
  print

  # Read file external
  f = file('cipher_msg.ci', 'r')
  for line in f:
    encrypted_msg = line.split(" ")
  f.close()

  # Decrypt messege from cipher text
  print bcolors.BOLD, "Decrypted message:", bcolors.ENDC
  print "Decrypting message with private key ", bcolors.OKBLUE ,private[0], private[1] ,"...", bcolors.ENDC
  start_time = timeit.default_timer()
  print

  # Decripting
  de_msg = decrypt(private, encrypted_msg)
  print de_msg
  for i in range(len(de_msg)/2+1):
    print "-",
  print
  elapsed_dec = timeit.default_timer() - start_time
  print "Decrypting time: ", elapsed_dec