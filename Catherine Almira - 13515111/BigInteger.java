/*
 * Nama : Catherine Almira
 * NIM  : 13515111
 * Kelas BigInteger adalah kelas yang merepresentasikan bilangan integer
 * dengan jumlah digit yang besar.
 */

import java.util.ArrayList;

public class BigInteger {
  private ArrayList<Integer> value;
  private int sign;

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
   * Mengembalikan hasil pembagian b1 / b2 tanpa memerhatikan sign.
   */

  public static BigInteger divideAbs(BigInteger b1, BigInteger b2) throws ArithmeticException {
  	if (b2.sign == 0) {
  	  throw new ArithmeticException("divide by zero");
  	}
  	if (b1.sign == 0) {
  	  return new BigInteger();
  	}
  	if (b1.abs().compareTo(b2.abs()) == -1) { //b1 < b2
  	  return new BigInteger();
  	}
  	BigInteger hasil = new BigInteger();
    BigInteger temp = new BigInteger();
    BigInteger zero = new BigInteger();
    int count = 0;
    hasil.sign = 1;
    temp.sign = 1;
    for (int i = b1.value.size() - b2.value.size(); i < b1.value.size(); i++) {
      temp.value.add(b1.value.get(i));
    }
    for (int i = b1.value.size() - b2.value.size(); i >= 0; i--) {
      while (temp.subtract(b2).compareTo(zero) > 0) {
        count++;
        temp = temp.subtract(b2);
      }
      if (i > 0) {
      	temp.value.add(0, b1.value.get(i - 1));
      }
      hasil.value.add(0, count);
      count = 0;
    }
    normalize(hasil);
  	return hasil;
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
  	if (b1.compareTo(b2) == 0) {
  	  BigInteger one = new BigInteger();
  	  one.sign = 1;
  	  one.value.add(1);
  	  return one;
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
      	  BigInteger temp1 = divide(b1, b2);
      	  BigInteger temp2 = multiply(temp1, b2);
      	  hasil = b1.subtract(temp2);
      	}
      }
      if (b2.sign == -1) {
      	if (b1.compareTo(b2.negate()) < 1) {
      	  hasil = b2.add(b1);
      	} else {
      	  BigInteger temp1 = divide(b1, b2.negate());
      	  BigInteger temp2 = multiply(temp1, b2.negate());
      	  BigInteger temphasil = b1.subtract(temp2);
      	  hasil = b2.add(temphasil);
      	}
      }
  	}
    if (b1.sign == -1) {
      if (b2.sign == 1) {
      	if (b1.negate().compareTo(b2) < 1) {
      	  hasil = b2.add(b1);
      	} else {
      	  BigInteger temp1 = divide(b1.negate(), b2);
      	  BigInteger temp2 = multiply(temp1, b2);
      	  BigInteger temphasil = b1.negate().subtract(temp2);
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
      	  BigInteger zero = new BigInteger();
      	  if (hasil.compareTo(zero) != 0) {
      	  	hasil.sign = -1;
      	  }
      	}
      }
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

  public static void main(String args[]) {
    BigInteger bi = new BigInteger("1512314234134");
    
    BigInteger b3 = new BigInteger("3134123");
    BigInteger a = mod(bi, b3);
    System.out.println(a);
    

  }
}

