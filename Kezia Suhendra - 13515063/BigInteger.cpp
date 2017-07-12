//NIM      : 13515063
//Nama     : Kezia Suhendra

#include "BigInteger.h"
#include "UtilsMsg.h"

BigInt::BigInt() {
  for (int i = 0; i < _capacity; ++i) {
    num[i] = 0;
  }
  sign = true;
}

BigInt::BigInt(const int& in) {
  for (int i = 0; i < _capacity; ++i) {
    num[i] = 0;
  }
  num[0] = in;
  if (in >= 0) {
    sign = true;
  } else {
    sign = false;
  }
}

BigInt::BigInt(const BigInt& in) {
  for (int i = 0; i < _capacity; ++i) {
    num[i] = in.num[i];
  }
  sign = in.sign;
}

BigInt::BigInt(string str, int base) {
  switch (base) {
    case BIN_STRING:
      GenFromBinString(str);
      break;
    case BYTE_STRING:
      GenFromByteString(str);
      break;
    case HEX_STRING:
      GenFromHexString(str);
      break;
    default:
      LOGLN("Big Integer Error base");
      exit(1);
      break;
  }
}

void BigInt::GenFromHexString(string str) {
  Clear();
  int idx = 0;
  while (str.length() > 8) {
    string segment = str.substr(str.length() - 8, 8);
    str.erase(str.length() - 8, 8);
    unsigned int current = 0;
    for (int i = 0; i < 8; ++i) {
      current = current * 16 + HexCharToInt(segment[i]);
    }
    num[idx++] = current;
  }
  unsigned int current = 0;
  for (int i = 0; i < str.length(); ++i) {
    current = current * 16 + HexCharToInt(str[i]);
    num[idx] = current;
  }
}

void BigInt::GenFromBinString(string str) {
  Clear();
  int idx = 0;
  while (str.length() > 32) {
    string segment = str.substr(str.length() - 32, 32);
    str.erase(str.length() - 32, 32);
    unsigned int current = 0;
    for (int i = 0; i < 32; ++i) {
      current = current * 2 + (segment[i] - '0');
    }
    num[idx++] = current;
  }
  unsigned int current = 0;
  for (int i = 0; i < str.length(); ++i) {
    current = current * 2 + (str[i] - '0');
  }
  num[idx] = current;
}

void BigInt::GenFromByteString(const string& buf) {
  int len = buf.length();
  int s1, s2, s3, s4;
  Clear();
  int mask[] = {0, 8, 16, 24};
  int valid = 0xff;
  size_t currentmask = 0;
  size_t current = 0;
  for (int i = 0; i < buf.size(); ++i) {
    int temp = (int) buf[i];
    num[current] |= ((temp << mask[currentmask]) & (valid << mask[currentmask]));
    currentmask++;
    if (currentmask == 4) {
      currentmask = 0;
      current++;
    }
  }
}

string BigInt::ToString() const {
  string res;
  int len = GetLength();
  char ch;
  unsigned int f4 = 0xFF, f3 = 0xFF00, f2 = 0xFF0000, f1 = 0xFF000000;
  for (int i = 0; i < len - 1; i++) {
    ch = (char)(num[i] & f4);
    res += ch;
    ch = (char)((num[i] & f3) >> 8);
    res += ch;
    ch = (char)((num[i] & f2) >> 16);
    res += ch;
    ch = (char)((num[i] & f1) >> 24);
    res += ch;
  }
  unsigned int temp = num[len - 1];
  while(temp) {
    ch = (char)(temp & f4);
    res += ch;
    temp >>= 8;
  }
  return res;
}

string BigInt::ToHexString() const {
  unsigned int temp, result;
  unsigned int filter = 0xf0000000;
  string resStr;
  for(int i = GetLength() - 1; i >= 0; i--) {
    temp = num[i];
    for (int j = 0; j < 8; j++) {
      result = temp & filter;
      result = (result >> 28);
      temp = (temp << 4);
      if (result >= 0 && result <= 9) {
        resStr += (result + '0');
      } else {
        switch (result) {
          case 10:
            resStr += 'A';
            break;
          case 11:
            resStr += 'B';
            break;
          case 12:
            resStr += 'C';
            break;
          case 13:
            resStr += 'D';
            break;
          case 14:
            resStr += 'E';
            break;
          case 15:
            resStr += 'F';
            break;
        }
      }
    }
  }
  while (resStr[0] == '0') {
    resStr.erase(0,1);
  }
  return resStr;
}

BigInt& BigInt::operator= (const BigInt& in) {
  for (int i = 0; i < _capacity; i++) {
    num[i] = in.num[i];
  }
  sign = in.sign;
  return *this;
}

BigInt& BigInt::operator>> (const int& a) {
  unsigned int bit, filter;
  filter = (1 << a) - 1;
  num[0] = (num[0] >> a);
  for (int i = 1; i < GetLength(); i++) {
    bit = num[i] & filter;
    bit = bit << (32 - a);
    num[i-1] = num[i-1] | bit;
    num[i] = (num[i] >> a);
  }
  return *this;
}

BigInt& BigInt::operator<< (const int& a) {
  unsigned int bit, filter;
  filter = (1 << a) - 1;
  filter = (filter << (32 - a));
  int len = GetLength();
  for (int i = len - 1; i >= 0; i--) {
    bit = num[i] & filter;
    bit = bit >> (32 - a);
    num[i + 1] = num[i + 1] | bit;
    num[i] = (num[i] << a);
  }
  return *this;
}

int BigInt::GetBitLength() const {
  int len = GetLength();
  int res = (len - 1) * 32;
  unsigned int temp = num[len - 1];
  while (temp > 0) {
    res++;
    temp = (temp >> 1);
  }
  return res;
}

int BigInt::GetLength() const {
  int length = _capacity;
  for (int i = _capacity - 1; i >= 0; i--) {
    if (num[i] == 0) {
      length--;
    } else {
      break;
    }
  }
  if (length == 0) {
    length = 1;
  }
  return length;
}

void BigInt::Clear() {
  for (int i = 0; i < _capacity; i++) {
    num[i] = 0;
  }
}

void BigInt::Random(int digit) {
  if (digit < 32) {
    num[0] = (rand() << 17) + (rand() << 2) + rand() % 4;
    unsigned int filter = 1;
    filter = (1 << digit) - 1;
    num[0] = (num[0] & filter);
    return;
  }
  for (int i = 0; i < digit / 32; i++) {
    num[i] = (rand() << 17) + (rand() << 2) + rand() % 4;
  }
  num[digit/32-1] = num[digit/32-1] | 0x80000000;
}

void BigInt::Randomsmall(int digit) {
  if (digit < 128) {
    num[0] = (rand() << 17) + (rand() << 2) + rand() % 4;
    unsigned int filter = 1;
    int temp = digit / 4;
    filter = (1 << temp) - 1;
    num[0] = (num[0] & filter);
    return;
  }
  for (int i = 0; i < digit / 128; i++) {
    num[i] = (rand() << 17) + (rand() << 2) + rand() % 4;
  }
  num[digit/128-1] = num[digit/128-1] | 0x80000000;
}

BigInt BigInt::operator+ (const BigInt& in) const {
  BigInt res;
  unsigned long long sum;
  unsigned int carry = 0;
  unsigned int sub;
  int length = (this->GetLength() >= in.GetLength()? this->GetLength() : in.GetLength());
  if (this->sign == in.sign) {
    for (int i = 0; i < length; i++) {
      sum = (unsigned long long)this->num[i] + in.num[i] + carry;
      res.num[i] = (unsigned int)sum;
      carry = (sum >> 32);
    }
    res.sign = this->sign;
    return res;
  } else {
    BigInt temp1, temp2;
    if (*this < in) {
      temp1 = in;
      temp2 = *this;
    } else {
      temp1 = *this;
      temp2 = in;
    }
    for (int i = 0; i < length; i++) {
      sub = temp2.num[i] + carry;
      if (temp1.num[i] >= sub) {
        res.num[i] = temp1.num[1] - sub;
        carry = 0;
      } else {
        res.num[i] = (unsigned long long)temp1.num[i] + ((unsigned long long)1 << 32) - sub;
        carry = 1;
      }
    }
    res.sign = temp1.sign;
    return res;
  }
}

BigInt BigInt::operator- (const BigInt& in) const {
  BigInt res;
  unsigned long long sum;
  unsigned int carry = 0, sub;
  if (this->sign == in.sign) {
    BigInt temp1, temp2;
    if (*this < in) {
      temp1 = in;
      temp2 = *this;
      temp1.sign = !temp1.sign;
    } else {
      temp1 = *this;
      temp2 = in;
    }
    for (int i = 0; i < _capacity; i++) {
      sub = temp2.num[i] + carry;
      if (temp1.num[i] >= sub) {
        res.num[i] = temp1.num[i] - sub;
        carry = 0;
      } else {
        res.num[i] = (unsigned long long)temp1.num[1] + ((unsigned long long)1 << 32) - sub;
        carry = 1;
      }
    }
    res.sign = temp1.sign;
    return res;
  } else {
    for (int i = 0; i < _capacity; i++) {
      sum = (unsigned long long)this->num[i] + in.num[i] + carry;
      res.num[i] = (unsigned int)sum;
      carry = (sum >> 32);
    }
    res.sign = this->sign;
    return res;
  }
}

BigInt BigInt::operator- (const int& in) const {
  BigInt temp(in);
  BigInt res = *this - temp;
  return res;
}

BigInt BigInt::operator* (const BigInt& in) const {
  BigInt res, last, temp;
  unsigned long long sum;
  unsigned int carry;
  for (int i = 0; i < in.GetLength(); i++) {
    carry = 0;
    for (int j = 0; j < this->GetLength() + 1; j++) {
      sum = ((unsigned long long)this->num[j]) * in.num[i] + carry;
      if ((i + j) < _capacity) {
        temp.num[i+j] = (unsigned int)sum;
      }
      carry = (sum >> 32);
    }
    res = (temp + last);
    last = res;
    temp.Clear();
  }
  if (this->sign == in.sign) {
    res.sign = true;
  } else {
    res.sign = false;
  }
  return res;
}

BigInt BigInt::operator* (const unsigned int& in) const {
  BigInt res;
  unsigned long long sum;
  unsigned int carry = 0;
  for (int i = 0; i < _capacity; i++) {
    sum = ((unsigned long long)this->num[i]) * in + carry;
    res.num[i] = (unsigned int)sum;
    carry = (sum >> 32);
  }
  res.sign = this->sign;
  return res;
}

BigInt BigInt::operator% (const BigInt& in) const {
  unsigned int mul, low, high;
  BigInt dividend, quotient, sub, subsequent;
  int length1 = this->GetLength();
  int length2 = in.GetLength();
  if (*this < in) {
    dividend = *this;
    dividend.sign = this->sign;
    return dividend;
  }
  for (int i = 0; i < length2; i++) {
    dividend.num[i] = this->num[length1-length2+i];
  }
  for (int i = length1 - length2; i >= 0; i--) {
    if (dividend < in) {
      for (int j = length2; j > 0; j--) {
        dividend.num[j] = dividend.num[j-1];
      }
      dividend.num[0] = this->num[i-1];
      continue;
    }
    low = 0;
    high = 0xffffffff;
    while (low <= high) {
      mul = (((unsigned long long)high) + low) / 2;
      sub = (in * mul);
      subsequent = (in * (mul + 1));
      if (((sub < dividend) && (subsequent > dividend)) || (sub == dividend)) {
        break;
      }
      if (subsequent == dividend) {
        mul++;
        sub = subsequent;
        break;
      }
      if ((sub < dividend) && (subsequent < dividend)) {
        low = mul;
        continue;
      }
      if ((sub > dividend) && (subsequent > dividend)) {
        high = mul;
        continue;
      }
    }
    quotient.num[i] = mul;
    dividend = dividend - sub;
    if ((i - 1) >= 0) {
      for (int j = length2; j > 0; j--) {
        dividend.num[j] = dividend.num[j-1];
      }
      dividend.num[0] = this->num[i-1];
    }
  }
  dividend.sign = this->sign;
  return dividend;
}

int BigInt::operator% (const int& in) const {
  int len = this->GetLength();
  if (len == 1) {
    return this->num[0] % in;
  }
  unsigned long long current = 0;
  for (int i = len - 1; i >= 0; i--) {
    current = (current << 32) + this->num[i];
    current = current % in;
  }
  return (int)current;
}

BigInt BigInt::operator/ (const BigInt& in) const {
  unsigned int mul, low, high;
  BigInt dividend, quotient, sub, subsequent;
  int length1 = this->GetLength();
  int length2 = in.GetLength();
  if (*this < in) {
    if (this->sign == in.sign) {
      quotient.sign = true;
    } else {
      quotient.sign = false;
    }
    return quotient;
  }
  for (int i = 0; i < length2; i++) {
    dividend.num[i] = this->num[length1-length2+i];
  }
  for (int i = length1 - length2; i >= 0; i--) {
    if (dividend < in) {
      for (int j = length2; j > 0; j--) {
        dividend.num[j] = dividend.num[j-1];
      }
      dividend.num[0] = this->num[i-1];
      continue;
    }
    low = 0;
    high = 0xffffffff;
    while (low < high) {
      mul = (((unsigned long long)high) + low) / 2;
      sub = (in * mul);
      subsequent = (in * (mul + 1));
      if (((sub < dividend) && (subsequent > dividend)) || (sub == dividend)) {
        break;
      }
      if (subsequent == dividend) {
        mul++;
        sub = subsequent;
        break;
      }
      if ((sub < dividend) && (subsequent < dividend)) {
        low = mul;
        continue;
      }
      if ((sub > dividend) && (subsequent > dividend)) {
        high = mul;
        continue;
      }
    }
    quotient.num[i] = mul;
    dividend = dividend - sub;
    if ((i - 1) >= 0) {
      for (int j = length2; j > 0; j--) {
        dividend.num[j] = dividend.num[j-1];
      }
      dividend.num[0] = this->num[i-1];
    }
  }
  if (this->sign == in.sign) {
    quotient.sign = true;
  } else {
    quotient.sign = false;
  }
  return quotient;
}

BigInt BigInt::operator& (const BigInt& in) const {
  int len = max(this->GetLength(), in.GetLength());
  BigInt res;
  for (int i = 0; i < len; i++)
      res.num[i] = (this->num[i] & in.num[i]);
  res.sign = (this->sign & in.sign);
  return res;
}

BigInt BigInt::operator^ (const BigInt& in) const {
  int len = max(this->GetLength(), in.GetLength());
  BigInt res;
  for (int i = 0; i < len; i++)
      res.num[i] = (this->num[i] ^ in.num[i]);
  res.sign = (this->sign ^ in.sign);
  return res;
}

BigInt BigInt::operator| (const BigInt& in) const {
  int len = max(this->GetLength(), in.GetLength());
  BigInt res;
  for (int i = 0; i < len; i++)
      res.num[i] = (this->num[i] ^ in.num[i]);
  res.sign = (this->sign ^ in.sign);
  return res;
}

bool BigInt::operator< (const BigInt& in) const {
  for (int i = _capacity-1; i > 0; i--) {
    if (this->num[i] < in.num[i]) {
      return true;
    }
    if (this->num[i] > in.num[i]) {
      return false;
    }
  }
  return this->num[0] < in.num[0];
}

bool BigInt::operator> (const BigInt& in) const {
  for (int i = _capacity-1; i >= 0; i--) {
    if (this->num[i] > in.num[i]) {
      return true;
    }
    if (this->num[i] < in.num[i]) {
      return false;
    }
  }
  return false;
}

bool BigInt::operator<= (const int& in) const {
  for (int i = 1; i < this->GetLength(); i++) {
    if (this->num[i] != 0) {
      return false;
    }
  }
  if (this->num[0] <= in) {
    return true;
  } else {
    return false;
  }
}

bool BigInt::operator== (const BigInt& in) const {
  for (int i = 0; i < _capacity; i++) {
    if (this->num[i] != in.num[i]) {
      return false;
    }
  }
  return true;
}

bool BigInt::operator== (const int& in) const {
  for (int i = 1; i < this->GetLength(); i++) {
    if (this->num[i] != 0) {
      return false;
    }
  }
  return this->num[0] == in;
}

ostream& operator<< (ostream& os, const BigInt& x) {
  x.output(os);
  return os;
}

BigInt BigInt::ModPow(const BigInt& n, const BigInt& p, const BigInt& m) {
  BigInt temp = p;
  BigInt r = n % m;
  BigInt k(1);
  while (!(temp <= 1)) {
    if (temp.IsOdd()) {
      k = (k * r) % m;
    }
    r = (r * r) % m;
    temp >> 1;
  }
  return (r * k) % m;
}

BigInt BigInt::Gcd(const BigInt& m, const BigInt& n) {
  if (n == 0) {
    return m;
  } else {
    return Gcd(n, m%n);
  }
}

BigInt BigInt::Euc(BigInt& E, BigInt& A) {
  BigInt M, X, Y, I, J;
  int x, y;
  M = A;
  X = 0;
  Y = 1;
  x = y = 1;
  while ((E.GetLength() != 1) || !(E == 0)) {
    I = M / E;
    J = M % E;
    M = E;
    E = J;
    J = Y;
    Y = Y * I;
    if (x == y) {
      if (X > Y) {
        Y = X - Y;
      } else {
        Y = Y - X;
        y = 0;
      }
    } else {
      Y = X + Y;
      x = 1 - x;
      y = 1 - y;
    }
    X = J;
  }
  if (x == 0) {
    X = A - X;
  }
  return X;
}

BigInt BigInt::GcdExtended(const BigInt& a, const BigInt& b, BigInt& x, BigInt&
y) {
  BigInt x0 = 1, y0 = 0, x1 = 0, y1 = 1, c = a, d = b;
  BigInt k, r, t;
  while (!(d == 0)) {
    k = c / d;
    r = c % d;
    t = c;
    c = d;
    d = r;
    t = x0;
    x0 = x1;
    x1 = t - k * x1;
    t = y0;
    y0 = y1;
    y1 = t - k * y1;
  }
  x = x0;
  y = y0;
  return c;
}

void BigInt::output(ostream& out) const {
  unsigned int temp, result;
  unsigned int filter = 0xf0000000;
  string resStr;
  for (int i = GetLength() - 1; i >= 0; i--) {
    temp = num[i];
    for (int j = 0; j < 8; j++) {
      result = temp & filter;
      result = (result >> 28);
      temp = (temp << 4);
      if (result >= 0 && result <= 9) {
        resStr += (result + '0');
      } else {
        switch (result) {
          case 10:
            resStr += 'A';
            break;
          case 11:
            resStr += 'B';
            break;
          case 12:
            resStr += 'C';
            break;
          case 13:
            resStr += 'D';
            break;
          case 14:
            resStr += 'E';
            break;
          case 15:
            resStr += 'F';
            break;
        }
      }
    }
  }
  while (resStr[0] == '0') {
    resStr.erase(0,1);
  }
  out << resStr;
}

int BigInt::HexCharToInt(char c) {
  if (c >= '0' && c <= '9') {
    return c - '0';
  } else switch (c) {
    case 'a':
    case 'A':
      return 10;
    case 'b':
    case 'B':
      return 11;
    case 'c':
    case 'C':
      return 12;
    case 'd':
    case 'D':
      return 13;
    case 'e':
    case 'E':
      return 14;
    case 'f':
    case 'F':
      return 15;
    default:
      break;
  }
  LOGLN("HexCharToInt error");
  exit(0);
}

char BigInt::IntToHexChar(int n) {
  char c;
  if (n >= 0 && n <= 9) {
    c = '0' + n;
  } else switch(n) {
    case 10:
      c = 'A';
      break;
    case 11:
      c = 'B';
      break;
    case 12:
      c = 'C';
      break;
    case 13:
      c = 'D';
      break;
    case 14:
      c = 'E';
      break;
    case 15:
      c = 'F';
      break;
    default:
      break;
  }
  return c;
}
