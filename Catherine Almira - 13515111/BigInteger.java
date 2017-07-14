/*
 * Nama : Catherine Almira
 * NIM  : 13515111
 * Kelas BigInteger adalah kelas yang merepresentasikan bilangan integer
 * dengan jumlah digit yang besar.
 */

import java.util.ArrayList;

import java.util.List;

import java.util.Random;

public class BigInteger {
  private ArrayList<Integer> value;
  private int sign;
  private static BigInteger zero = new BigInteger();
  private static BigInteger one = new BigInteger("1");
  private static BigInteger two = new BigInteger("2");

  /*
   * Menghilangkan angka 0 di depan BigInteger b.
   */

  private static void normalize(BigInteger b) {
    if (b.value.size() > 0) {
      if (b.value.get(b.value.size() - 1) != 0) {

      } else {
        b.value.remove(b.value.size() - 1);
        normalize(b);
        if (b.value.size() == 0) {
          b.sign = 0;
        }
      }
    }
  }

  /*
   * Konstruktor tanpa parameter.
   * Menciptakan BigInteger dengan nilai 0.
   */
  
  public BigInteger() {
    value = new ArrayList<Integer>();
    sign = 0;
  }

  /*
   * Konstruktor dengan parameter.
   */

  public BigInteger(String val) {
  	value = new ArrayList<Integer>();
  	if (val.length() == 1 && val.charAt(0) == '0') {
      sign = 0;
    } else if (val.charAt(0) == '-') {
      sign = -1;
      for (int i = val.length() - 1; i >= 1; i--) {
      	value.add(val.charAt(i) - '0');
      }
    } else {
      sign = 1;
      for (int i = val.length() - 1; i >= 0; i--) {
      	value.add(val.charAt(i) - '0');
      }
    }
  }
  
  /*
   * Konstruktor dengan parameter.
   */

  public BigInteger(BigInteger b) {
    value = new ArrayList<Integer>(b.value);
    sign = b.sign;
  }

  /*
   * Mengembalikan -1 apabila nilai lebih kecil,
   * 0 apabila sama dengan, dan
   * 1 apabila lebih besar.
   */

  public int compareTo(BigInteger b) {
  	if (sign > b.sign) {
  	  return 1;
  	} else if (sign < b.sign) {
  	  return -1;
  	} else { //sign = b.sign
      if (sign == 0) {
      	return 0;
      } else if (sign == -1) {
      	if (value.size() < b.value.size()) {
      	  return 1;
      	} else if (value.size() > b.value.size()) {
      	  return -1;
      	} else {
      	  int i = value.size() - 1;
      	  boolean found = false;
      	  while (i >= 0 && !found) {
      	  	if (value.get(i) != b.value.get(i)) {
      	  	  found = true;
      	  	} else {
      	  	  i--;
      	  	}
      	  }
      	  if (!found) {
      	  	return 0;
      	  } else {
      	  	if (value.get(i) < b.value.get(i)) {
      	  	  return 1;
      	  	} else {
      	  	  return -1;
      	  	}
      	  }
      	}
      } else { //sign = 1
      	if (value.size() > b.value.size()) {
      	  return 1;
      	} else if (value.size() < b.value.size()) {
      	  return -1;
      	} else {
      	  int i = value.size() - 1;
      	  boolean found = false;
      	  while (i >= 0 && !found) {
      	  	if (value.get(i) != b.value.get(i)) {
      	  	  found = true;
      	  	} else {
      	  	  i--;
      	  	}
      	  }
      	  if (!found) {
      	  	return 0;
      	  } else {
      	  	if (value.get(i) > b.value.get(i)) {
      	  	  return 1;
      	  	} else {
      	  	  return -1;
      	  	}
      	  }
      	}
      }
  	}
  }
  
  /*
   * Mengembalikan nilai -this.
   */

  public BigInteger negate() {
  	BigInteger hasil = new BigInteger(this);
  	hasil.sign = -sign;
  	return hasil;
  }

  /*
   * Mengembalikan hasil penjumlahan this + b tanpa memerhatikan sign.
   */

  public BigInteger plus(BigInteger b) {
    BigInteger hasil = new BigInteger();
    hasil.sign = 1;
    int carry = 0;
    int temp;
    if (value.size() == b.value.size()) {  
      for (int i = 0; i < value.size(); i++) {
        temp = value.get(i) + b.value.get(i) + carry;
        if (temp > 9) {
          carry = 1;
          hasil.value.add(temp - 10);
        } else {
          carry = 0;
          hasil.value.add(temp);
        }
      }
      if (carry > 0) {
      	hasil.value.add(carry);
      }
    }
    if (value.size() < b.value.size()) {
      for (int i = 0; i < value.size(); i++) {
      	temp = value.get(i) + b.value.get(i) + carry;
        if (temp > 9) {
          carry = 1;
          hasil.value.add(temp - 10);
        } else {
          carry = 0;
          hasil.value.add(temp);
        }
      }
      for (int i = value.size(); i < b.value.size(); i++) {
      	temp = b.value.get(i) + carry;
      	if (temp > 9) {
          carry = 1;
          hasil.value.add(temp - 10);
        } else {
          carry = 0;
          hasil.value.add(temp);
        }
      }
      if (carry > 0) {
      	hasil.value.add(carry);
      }
    }
    if (value.size() > b.value.size()) {
      for (int i = 0; i < b.value.size(); i++) {
      	temp = value.get(i) + b.value.get(i) + carry;
        if (temp > 9) {
          carry = 1;
          hasil.value.add(temp - 10);
        } else {
          carry = 0;
          hasil.value.add(temp);
        }
      }
      for (int i = b.value.size(); i < value.size(); i++) {
      	temp = value.get(i) + carry;
      	if (temp > 9) {
          carry = 1;
          hasil.value.add(temp - 10);
        } else {
          carry = 0;
          hasil.value.add(temp);
        }
      }
      if (carry > 0) {
      	hasil.value.add(carry);
      }
    }
    return hasil;
  }

  /*
   * Mengembalikan hasil pengurangan this - b tanpa memerhatikan sign.
   * Prekondisi: this > b.
   */

  public BigInteger minus(BigInteger b) {
  	BigInteger hasil = new BigInteger();
  	hasil.sign = 1;
  	int temp;
  	int borrow = 0;
  	if (value.size() == b.value.size()) {
      for (int i = 0; i < value.size(); i++) {
      	temp = value.get(i) - b.value.get(i) - borrow;
      	if (temp < 0) {
      	  borrow = 1;
      	  hasil.value.add(temp + 10); 
      	} else {
      	  borrow = 0;
      	  hasil.value.add(temp);
      	}
      }
  	} else if (value.size() > b.value.size()) {
      for (int i = 0; i < b.value.size(); i++) {
      	temp = value.get(i) - b.value.get(i) - borrow;
      	if (temp < 0) {
      	  borrow = 1;
      	  hasil.value.add(temp + 10); 
      	} else {
      	  borrow = 0;
      	  hasil.value.add(temp);
      	}
      }
      for (int i = b.value.size(); i < value.size(); i++) {
      	temp = value.get(i) - borrow;
      	if (temp < 0) {
      	  borrow = 1;
      	  hasil.value.add(temp + 10); 
      	} else {
      	  borrow = 0;
      	  hasil.value.add(temp);
      	}
      }
  	} else { //value.size() < b.value.size()
      //Seharusnya ga akan masuk ke sini.
  	}
  	normalize(hasil);
  	return hasil;
  }

  /*
   * Mengembalikan hasil penjumlahan this + b.
   */

  public BigInteger add(BigInteger b) {
    if (sign == 0) {
      return new BigInteger(b);
    }
    if (b.sign == 0) {
      return new BigInteger(this);
    }
    BigInteger hasil;
    if (sign == b.sign) {
      hasil = this.plus(b);
      if (sign == -1) {
      	hasil.sign = -1;
      }
      return hasil;
    } else {
      if (sign == 1) { //b.sign = -1
        if (this.compareTo(b.negate()) == 0) {
          hasil = this.minus(b);
          hasil.sign = 0;
        } else if (this.compareTo(b.negate()) > 0) { //this > b
          hasil = this.minus(b);
        } else { //this < b
          hasil = b.minus(this);
          hasil.sign = -1;
        }
      } else { //sign = -1 && b.sign = 1
        if (this.negate().compareTo(b) == 0) {
          hasil = this.minus(b);
          hasil.sign = 0;
        } else if (this.negate().compareTo(b) > 0) { //this > b
          hasil = this.minus(b);
          hasil.sign = -1;
        } else { //this < b
          hasil = b.minus(this);
        }
      }
    }
    return hasil;
  }

  /*
   * Mengembalikan hasil pengurangan this - b.
   */

  public BigInteger subtract(BigInteger b) {
  	if (sign == 0) {
  	  return new BigInteger(b.negate());
  	}
  	if (b.sign == 0) {
  	  return new BigInteger(this);
  	}
  	BigInteger hasil;
  	if (sign == b.sign) {
  	  if (sign == 1) {
  	  	if (this.compareTo(b) == 0) {
  	  	  hasil = this.minus(b);
  	  	  hasil.sign = 0;
  	  	} else if (this.compareTo(b) > 0) {
  	  	  hasil = this.minus(b);
  	  	} else {
  	  	  hasil = b.minus(this);
  	  	  hasil.sign = -1;
  	  	}
  	  } else {
  	  	if (this.compareTo(b) == 0) {
  	  	  hasil = this.minus(b);
  	  	  hasil.sign = 0;
  	  	} else if (this.compareTo(b) > 0) {
  	  	  hasil = b.minus(this);
  	  	} else {
  	  	  hasil = this.minus(b);
  	  	  hasil.sign = -1;
  	  	}
  	  }
  	} else {
  	  hasil = this.plus(b);
  	  if (sign == -1) {
        hasil.sign = -1;
  	  }
  	}
  	return hasil;
  }

  /*
   * Mengembalikan hasil dari perkalian this * 10 ^ n.
   */

  public BigInteger powOfTen(int n) {
  	if (sign == 0) {
      return new BigInteger();
    }
    BigInteger hasil = new BigInteger();
    hasil.sign = sign;
    for (int i = 0; i < n; i++) {
      hasil.value.add(0);
    }
    hasil.value.addAll(value);
    return hasil;
  }
  
  /*
   * Mengembalikan nilai terbesar di antara a dan b.
   */

  private static int max(int a, int b) {
    return (a > b ? a : b);
  }
  
  /*
   * Mengembalikan nilai BigInteger setelah menghapuskan n digit terdepan.
   * Bernilai this mod 10 ^ n.
   */

  private BigInteger removeNDigitForward(int n) {
  	if (n == 0) {
  	  return new BigInteger(this);
  	} else if (n < 0) {
      return new BigInteger();
  	} else if (n < value.size()) {
  	  BigInteger hasil = new BigInteger();
  	  hasil.sign = sign;
  	  for (int i = 0; i < value.size() - n; i++) {
  	    hasil.value.add(value.get(i));
  	  }
  	  normalize(hasil);
  	  return hasil;
  	} else {
  	  return new BigInteger();
  	}
  }

  /*
   * Mengembalikan nilai BigInteger setelah menghapuskan n digit terbelakang.
   */

  private BigInteger removeNDigitBackWard(int n) {
  	
  	if (n == 0) {
  	  return new BigInteger(this);
  	} else if (n < value.size()) {
  	  BigInteger hasil = new BigInteger();
  	  hasil.sign = sign;
  	  for (int i = n; i < value.size(); i++) {
  	    hasil.value.add(value.get(i));
  	  }
  	  return hasil;
  	} else {
  	  return new BigInteger();
  	}
  }

  /*
   * Mengembalikan hasil perkalian b1 * b2.
   * Perkalian menggunakan algoritma karatsuba.
   * Prekondisi: b1 dan b2 bernilai positif.
   */

  public static BigInteger karatsuba(BigInteger b1, BigInteger b2) {
  	if (b1.sign == 0 || b2.sign == 0 || b1.value.size() == 0 || b2.value.size() == 0) {
  	  return new BigInteger();
  	}
  	if (b1.value.size() == 1 && b2.value.size() == 1) {
  	  return new BigInteger(String.valueOf(b1.value.get(0) * b2.value.get(0)));
  	}
  	int m = max(b1.value.size(), b2.value.size());
  	int m1 = m / 2;
  	BigInteger a = b1.removeNDigitBackWard(m1);
  	BigInteger b = b1.removeNDigitForward(a.value.size());
  	BigInteger c = b2.removeNDigitBackWard(m1);
  	BigInteger d = b2.removeNDigitForward(c.value.size());
  	BigInteger z0 = karatsuba(a, c);
  	BigInteger z1 = karatsuba(b, d);
  	BigInteger z2 = karatsuba(a.add(b), c.add(d)).subtract(z0).subtract(z1);
  	return (z0.powOfTen(2 * m1).add(z2.powOfTen(m1)).add(z1));
  }

  /*
   * Mengembalikan hasil perkalian b1 * b2.
   */

  public static BigInteger multiply(BigInteger b1, BigInteger b2) {
  	if (b1.sign == 0 || b2.sign == 0) {
  	  return new BigInteger();
  	}
  	BigInteger hasil = new BigInteger();
  	hasil = karatsuba(b1, b2);
  	if (b1.sign == b2.sign) {
  	  hasil.sign = 1;
  	} else {
  	  hasil.sign = -1;
  	}
  	return hasil;
  }

  /*
   * Mengembalikan hasil perkalian this * b.
   */

  public BigInteger multiply(BigInteger b) {
    return multiply(this, b);
  }

  /*
   * Mengembalikan nilai absolut dari this.
   */

  public BigInteger abs() {
    if (sign == 0) {
      return new BigInteger();
    } else {
      BigInteger hasil = new BigInteger(this);
      hasil.sign = 1;
      return hasil;
    }
  }

  /*
   * Mengembalikan hasil pembagian dan modulus pada array of BigInteger.
   * indeks 0 untuk hasil bagi dan indeks 1 untuk sisa hasil bagi (mod).
   */

  public static BigInteger[] divideModAbs(BigInteger b1, BigInteger b2) throws ArithmeticException {
    if (b2.sign == 0) {
      throw new ArithmeticException("divide by zero");
    }
    BigInteger[] hasil = new BigInteger[2];
    for (int i = 0; i < 2; i++) {
      hasil[i] = new BigInteger();
    }
    if (b1.sign == 0) {
      return hasil;
    }
    if (b1.abs().compareTo(b2.abs()) == -1) { //b1 < b2
      hasil[1] = new BigInteger(b1);
    }
    BigInteger tempDiv = new BigInteger();
    BigInteger tempMod = new BigInteger();
    int count = 0;
    tempDiv.sign = 1;
    tempMod.sign = 1;
    for (int i = b1.value.size() - b2.value.size(); i < b1.value.size(); i++) {
      tempMod.value.add(b1.value.get(i));
    }
    for (int i = b1.value.size() - b2.value.size(); i >= 0; i--) {
      normalize(tempMod);
      while (tempMod.subtract(b2).compareTo(zero) >= 0) {
        count++;
        tempMod = tempMod.subtract(b2);
      }
      if (i > 0) {
        if (tempMod.sign == 0) {
          tempMod.sign = 1;
        }
        tempMod.value.add(0, b1.value.get(i - 1));
      }
      tempDiv.value.add(0, count);
      count = 0;
    }
    normalize(tempDiv);
    normalize(tempMod);
    hasil[0] = tempDiv;
    hasil[1] = tempMod;
    return hasil;
  }

  /*
   * Mengembalikan hasil pembagian b1 / b2 tanpa memerhatikan sign.
   */

  public static BigInteger divideAbs(BigInteger b1, BigInteger b2) throws ArithmeticException {
  	return new BigInteger(divideModAbs(b1, b2)[0]);
  }

  /*
   * Mengembalikan hasil pembagian this / b.
   */

  public static BigInteger divide(BigInteger b1, BigInteger b2) throws ArithmeticException {
    if (b2.sign == 0) {
  	  throw new ArithmeticException("divide by zero");
  	}
  	if (b1.sign == 0) {
  	  return new BigInteger();
  	}
    if (b1.compareTo(b2) < 0) {
      return new BigInteger();
    }
  	if (b1.compareTo(b2) == 0) {
  	  return new BigInteger(one);
  	}
  	BigInteger hasil = divideAbs(b1, b2);
    if (b1.sign != b2.sign) {
      hasil.sign = -1;
    } else {
      hasil.sign = 1;
    }
  	return hasil;
  }

  /*
   * Mengembalikan nilai modulus b1 mod b2.
   * Prekondisi: b1 dan b2 bilangan positif.
   */

  public static BigInteger modAbs(BigInteger b1, BigInteger b2) throws ArithmeticException {
    return new BigInteger(divideModAbs(b1, b2)[1]);
  }

  /*
   * Mengembalikan nilai modulus b1 mod b2.
   */

  public static BigInteger mod(BigInteger b1, BigInteger b2) throws ArithmeticException {
  	if (b2.sign == 0) { //b1 mod 0
  	  throw new ArithmeticException("mod by zero");
  	}
  	if (b1.sign == 0) { //0 mod b2
  	  return new BigInteger();
  	}
  	BigInteger hasil = new BigInteger();
  	if (b1.sign == 1) {
      if (b2.sign == 1) {
      	if (b1.compareTo(b2) == -1) {
      	  return b1;
      	} else {
      	  hasil = modAbs(b1, b2);
      	}
      }
      if (b2.sign == -1) {
      	if (b1.compareTo(b2.negate()) < 1) {
      	  hasil = b2.add(b1);
      	} else {
          BigInteger temphasil = modAbs(b1, b2.negate());
      	  hasil = b2.add(temphasil);
      	}
      }
  	}
    if (b1.sign == -1) {
      if (b2.sign == 1) {
      	if (b1.negate().compareTo(b2) < 1) {
      	  hasil = b2.add(b1);
      	} else {
      	  BigInteger temphasil = modAbs(b1.negate(), b2.negate());
      	  hasil = b2.subtract(temphasil);
      	}
      }
      if (b2.sign == -1) {
      	if (b1.compareTo(b2) == 1) {
      	  return b1;
      	} else {
      	  BigInteger temp1 = divide(b1.negate(), b2.negate());
      	  BigInteger temp2 = multiply(temp1, b2.negate());
      	  hasil = b1.negate().subtract(temp2);
      	  if (hasil.compareTo(zero) != 0) {
      	  	hasil.sign = -1;
      	  }
      	}
      }
  	}
  	return hasil;
  }

  /*
   * Mengembalikan nilai pembagi terbesar bilangan b1 dan b2.
   */

  public static BigInteger gcd(BigInteger b1, BigInteger b2) {
    BigInteger abs1 = b1.abs();
    BigInteger abs2 = b2.abs();
    if (abs1.sign == 0) {
      return new BigInteger(abs2);
    } else {
      return gcd(mod(abs2, abs1), abs1);
    }
  }

  /*
   * Mengembalikan array BigInteger [x, y] dimana b1 * x + b2 * y = gcd(b1, b2).
   * Prekondisi: b1 dan b2 tidak negatif.
   */

  public static BigInteger[] gcdExtended(BigInteger b1, BigInteger b2) {
    BigInteger[] hasil = new BigInteger[2];
    if (b1.sign == 0) {
      hasil[0] = new BigInteger();
      hasil[1] = new BigInteger("1");
      return hasil;
    }
    BigInteger[] temp = gcdExtended(mod(b2, b1), b1);
    hasil[1] = temp[0];
    hasil[0] = temp[1].subtract(multiply(temp[0], divide(b2, b1)));
    return hasil;
  }
  
  /*
   * Mengembalikan nilai invers modulo b1^-1 mod b2.
   */

  public static BigInteger modInverse(BigInteger b1, BigInteger b2) throws ArithmeticException {
    if (b2.sign < 1) {
      throw new ArithmeticException("mod by zero");
    }
    BigInteger[] temp = gcdExtended(b1, b2);
    if (gcd(b1, b2).compareTo(one) != 0) {
      throw new ArithmeticException("not relative prime");
    }
    if (temp[0].sign == -1) {
      return b2.add(temp[0]);
    }
    return temp[0];
  }

  public BigInteger modInverse(BigInteger b) throws ArithmeticException {
    return modInverse(this, b);
  }
  
  /*
   * Mengembalikan akar dari BigInteger.
   * keterangan: nilai akar tidak presisi.
   */

  public static BigInteger sqrt(BigInteger b) {
    BigInteger temp;
    BigInteger hasil = divide(b, two);
    do {
      temp = hasil;
      hasil = divide((temp.add(divide(b, temp))), two);
    } while ((temp.subtract(hasil)).compareTo(zero) > 0);
    return hasil;
  }

  /*
   * Mengembalikan nilai b1 ^ b2 mod b3.
   */

  public static BigInteger modPow(BigInteger b1, BigInteger b2, BigInteger b3) throws ArithmeticException {
    if (b2.sign == -1) {
      throw new ArithmeticException("negative exponent");
    }
    if (b3.sign <= 0) {
      throw new ArithmeticException("negative modulo");
    }
    if (b1.sign == 0 && b2.sign == 0) {
      throw new ArithmeticException("undefine");
    }
    if (b1.sign == 0) {
      return new BigInteger();
    }
    if (b2.sign == 0) {
      if (b3.compareTo(one) > 0) {
        return new BigInteger(one);
      } else {
        return new BigInteger();
      }
      //hasil = mod(one, b3);
    }
    if (b2.compareTo(one) == 0) {
      return new BigInteger(mod(b1, b3));
    }
    BigInteger[] halfpow = divideModAbs(b2, two);
    BigInteger hasil = modPow(b1, halfpow[0], b3);
    hasil = multiply(hasil, hasil);
    if (halfpow[1].compareTo(zero) != 0) {
      hasil = multiply(hasil, b1);
    }
    return mod(hasil, b3);
    /*if (mod(b2, two).compareTo(zero) == 0) {
      hasil =  pow(mod(multiply(b1, b1), b3), divide(b2, two));
    } else {
      hasil = mod(multiply(b1, pow(mod(multiply(b1, b1), b3), divide(b2, two))), b3);
    }*/

    //return hasil;
  }

  /*
   * Mengembalikan true apabila this adalah BigInteger yang kemungkinan besar merupakan
   * bilangan prima dengan menggunakan Miller Rabin Test.
   */

  public static boolean isProbablePrime(BigInteger b) {
    if (b.compareTo(zero) == 0 || b.compareTo(one) == 0) { //b = 0 atau b = 1
      return false;
    }
    if (b.compareTo(two) == 0) { //b = 2
      return true;
    }
    if (mod(b, two).compareTo(zero) == 0) { //b mod 2 = 0
      return false;
    }
    BigInteger m = b.subtract(one);
    int k = 0;
    while (mod(m, two).compareTo(zero) == 0) {
      m = divide(m, two);
      k++;
    }
    BigInteger temp = modPow(two, m, b);
    if (temp.compareTo(one) == 0 || temp.compareTo(b.subtract(one)) == 0) {
      return true;
    }
    for (int i = 1; i < k; i++) {
      temp = modPow(temp, two, b);      
      if (temp.compareTo(one) == 0) {
        return false;
      } else if (temp.compareTo(b.subtract(one)) == 0) {
        return true;
      }
    }
    return false;
  }

  /*
   * Mengembalikan BigInteger ganjil secara acak dengan jumlah digit n.
   * Digit terakhir bukanlah angka 5.
   */

  public static BigInteger generateRandomEven(int n) {
    BigInteger hasil = new BigInteger();
    Random rand = new Random();
    int temp;
    hasil.sign = 1;
    //digit terakhir adalah bilangan ganjil yang bukan 5
    do {
      temp = rand.nextInt(5);
    } while (temp == 2);
    hasil.value.add(temp * 2 + 1);
    for (int i = 1; i < n; i++) {
      if (i == n - 1) { //digit pertama bukan angka 0
        temp = rand.nextInt(9) + 1;
      } else {
        temp = rand.nextInt(10);
      }
      hasil.value.add(temp);
    }
    return hasil;
  }

  /*
   * Mengembalikan BigInteger secara acak dengan jumlah digit n.
   */

  public static BigInteger generateRandom(int n) {
    if (n == 0) {
      return new BigInteger();
    }
    BigInteger hasil = new BigInteger();
    Random rand = new Random();
    int temp;
    hasil.sign = 1;
      for (int i = 0; i < n; i++) {
        if (i == n - 1) { //digit pertama bukan angka 0
          temp = rand.nextInt(9) + 1;
        } else {
          temp = rand.nextInt(10);
        }
        hasil.value.add(temp);
      }
    return hasil;
  }

  /*
   * Mengembalikan BigInteger prima secara acak dengan jumlah digit n.
   */

  public static BigInteger generateRandomPrime(int n) {
    BigInteger hasil = generateRandomEven(n);
    while (!isProbablePrime(hasil)) {
      hasil = hasil.add(two);
    }
    return hasil;
  }

  /*
   * Mengembalikan String yang merepresentasikan nilai BigInteger.
   */

  public String toString() {
  	String output = "";
  	if (sign == -1) {
  	  output = "-";
  	}
  	if (sign == 0) {
  	  output = "0";
  	} else {
  	  for (int i = value.size() - 1; i >= 0; i--) {
  	  	output += value.get(i);
  	  }
  	}
  	return output;
  }
}

