def BacaFile(namafile) :
    file = open(namafile, 'r')
    isifile = file.read()
    file.close()
    return isifile

def TulisFile(namafile, list):
    file = open(namafile, 'w')
    for i in list:
        file.write("%s\n" % i)

def KatatoASCII(kata) :
    asc = ''.join(str(ord(c)) for c in kata)
    return asc

def KatatoListASCII(kata) :
    list = []
    for c in kata:
        list.append(ord(c))
    return list

isifile = BacaFile('input.txt')
plaintext = KatatoASCII(isifile)
TulisFile('asciicip.txt', plaintext)