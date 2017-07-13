import java.util.Random;

public class BigNum {

	private int[] num;
	public static final BigNum ZERO = new BigNum();
	public static final BigNum ONE = new BigNum(new int[]{1});
	public static final BigNum TWO = new BigNum(new int[]{2});
	
	public BigNum() {
		num = new int[]{0};
	}
	
	public BigNum(int n) {
		num = new int[n];
		for (int i = 0; i < n; i++) {
			num[i] = 0;
		}
	}
	
	public BigNum(int[] input) {
		num = input;
	}
	
	private int[] normalize(int[] input) {
		int[] output = input;
		int i = output.length - 1;
		while (i >= 0 && output[i] == 0) {
			i--;
		}
		if (i >= 0 && i < output.length - 1) {
			int[] temp = output;
			output = new int[i + 1];
			for (int j = 0; j < i + 1; j++) {
				output[j] = temp[j];
			}
		}
		else if (i < 0) {
			output = new int[]{0};
		}
		return output;
	}
	
	private void shrink() {
		this.num = normalize(this.num);
	}
	
	public BigNum add(BigNum bn) {
		int[] res = (this.num.length > bn.num.length) ? this.num.clone() : bn.num.clone();
		int smallerLength = (this.num.length < bn.num.length) ? this.num.length : bn.num.length;
		long carry = 0, tempres = 0, num1 = 0, num2 = 0;
		for (int i = 0; i < smallerLength; i++) {
			num1 = this.num[i];
			num1 = (num1 << 0x20) >>> 0x20;
			num2 = bn.num[i];
			num2 = (num2 << 0x20) >>> 0x20;
			tempres = num1 + num2 + carry;
			carry = tempres >> 0x20;
			tempres = tempres & 0xFFFFFFFF;
			res[i] = (int) tempres;
		}
		int i = smallerLength;
		while (carry == 1 && i < res.length) {
			num1 = res[i];
			num1 = (num1 << 0x20) >>> 0x20;
			tempres = num1 + carry;
			carry = tempres >>> 0x20;
			tempres = tempres & 0xFFFFFFFF;
			res[i] = (int) tempres;
			i++;
		}
		if (carry == 1) {
			int[] temp = res;
			res = new int[temp.length + 1];
			for (int j = 0; j < temp.length; j++) {
				res[j] = temp[j];
			}
			res[temp.length] = (int) carry;
			carry = 0;
		}
		BigNum result = new BigNum(res);
		return result;
	}
	
	public BigNum subtract(BigNum bn) {
		int[] res = (this.num.length > bn.num.length) ? this.num.clone() : bn.num.clone();
		int smallerLength = (this.num.length < bn.num.length) ? this.num.length : bn.num.length;
		long borrow = 0, tempres = 0, num1 = 0, num2 = 0;
		for (int i = 0; i < smallerLength; i++) {
			num1 = this.num[i];
			num1 = (num1 << 0x20) >>> 0x20;
			num2 = bn.num[i];
			num2 = (num2 << 0x20) >>> 0x20;
			tempres = num1 - num2 - borrow;
			if (tempres < 0) {
				borrow = 1;
				tempres += 0xFFFFFFFF;
				tempres += 1;
			}
			else {
				borrow = 0;
			}
			res[i] = (int) tempres;
		}
		int i = smallerLength;
		while (borrow == 1 && i < res.length) {
			num1 = res[i];
			num1 = (num1 << 0x20) >>> 0x20;
			tempres = num1 - borrow;
			if (tempres < 0) {
				borrow = 1;
				tempres += 0xFFFFFFFF;
				tempres += 1;
			}
			else {
				borrow = 0;
			}
			res[i] = (int) tempres;
			i++;
		}
		res = normalize(res);
		BigNum result = new BigNum(res);
		return result;
	}
	
	public BigNum multiply(BigNum bn) {
		int[] res;
		BigNum finalres = new BigNum();
		for (int i = 0; i < bn.num.length; i++) {
			long carry = 0, tempres = 0, num1 = 0, num2 = 0;
			res = new int[this.num.length + 1];
			for (int j = 0; j < this.num.length; j++) {
				num1 = this.num[j];
				num1 = (num1 << 0x20) >>> 0x20;
				num2 = bn.num[i];
				num2 = (num2 << 0x20) >>> 0x20;
				tempres = num1 * num2 + carry;
				carry = tempres >>> 0x20;
				tempres = tempres & 0xFFFFFFFF;
				res[j] = (int) tempres;
			}
			if (carry != 0) {
				res[this.num.length] = (int) carry;
			}
			res = normalize(res);
			int[] temp = res;
			res = new int[temp.length + i];
			for (int k = 0; k < i; k++) {
				res[k] = 0;
			}
			for (int k = i; k < res.length; k++) {
				res[k] = temp[k - i];
			}
			res = normalize(res);
			finalres = finalres.add(new BigNum(res));
		}
		return finalres;
	}
	
	public BigNum[] division(BigNum bn) {
		BigNum q = new BigNum(this.num.length), r = new BigNum(), n = new BigNum(this.num.clone()), d = new BigNum(bn.num.clone());
		for (int i = (this.num.length * 32) - 1; i >= 0; i--) {
			r = r.shiftLeft(1);
			r = (n.getBit(i) == 1) ? r.setBit(0) : r.clearBit(0);
			if (r.compareTo(d) >= 0) {
				r = r.subtract(d);
				q = q.setBit(i);
			}
		}
		q.shrink();
		BigNum[] divmod = new BigNum[2];
		divmod[0] = q;
		divmod[1] = r;
		return divmod;
	}
	
	public BigNum divide(BigNum bn) {
		return division(bn)[0];
	}
	
	public int getBit(int i) {
		int index = i / 32;
		int offset = i % 32;
		int result = (num[index] >> offset) & 1;
		return result;
	}
	
	public BigNum setBit(int i) {
		int[] res = this.num.clone();
		int index = i / 32;
		int offset = i % 32;
		res[index] |= (1 << offset);
		BigNum result = new BigNum(res);
		return result;
	}
	
	public BigNum clearBit(int i) {
		int[] res = this.num.clone();
		int index = i / 32;
		int offset = i % 32;
		res[index] &= ~(1 << offset);
		BigNum result = new BigNum(res);
		return result;
	}
	
	public BigNum gcd(BigNum bn) {
		BigNum a = new BigNum(this.num.clone());
		BigNum b = new BigNum(bn.num.clone());
		BigNum temp = new BigNum();
		while (!b.equals(ZERO)) {
			temp = b;
			b = a.mod(b);
			a = temp;
		}
		return a;
	}
	
	public BigNum mod(BigNum bn) {
		return division(bn)[1];
	}
	
	public static BigNum genRandomOdd(int nBit) {
		Random rand = new Random();
		int index = 0;
		if (nBit % 32 == 0) {
			index = nBit / 32;
		}
		else {
			index = (nBit / 32) + 1;
		}
		int offset = 32 - (nBit % 32);
		int mask = 0xFFFFFFFF >>> offset;
		int[] res = new int[index];
		for (int i = 0; i < index; i++) {
			res[i] = rand.nextInt();
		}
		res[index - 1] &= mask;
		if (res[0] % 2 == 0) {
			++res[0];
		}
		BigNum result = new BigNum(res);
		result.shrink();
		return result;
	}
	
	public static BigNum genRandomInRange(BigNum start, BigNum end) {
		BigNum result = new BigNum();
		Random rand = new Random();
		int randLength = rand.nextInt((end.num.length - start.num.length) + 1) + start.num.length;
		result.num = new int[randLength];
		for (int i = 0; i < randLength; i++) {
			result.num[i] = rand.nextInt();
		}
		result = result.mod((end.subtract(start)).add(ONE)).add(start);
		return result;
	}
	
	private boolean millerTest(BigNum d) {
		BigNum a = genRandomInRange(TWO, this.subtract(TWO));
		BigNum x = a.modPow(d, this);
		BigNum thisminusone = this.subtract(ONE);
		if (x.equals(ONE) || x.equals(thisminusone)) {
			return true;
		}
		while (!d.equals(thisminusone)) {
			x = x.multiply(x).mod(this);
			d = d.multiply(TWO);
			if (x.equals(ONE)) {
				return false;
			}
			if (x.equals(thisminusone)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPrimeMR(int k) {
		BigNum d = this.subtract(ONE);
	    while (!d.isOdd()) {
	    	d = d.divide(TWO);
	    }
	    for (int i = 0; i < k; i++) {
	         if (millerTest(d) == false) {
	        	 return false;
	         }
	    }
	    return true;
	}
	
	public static BigNum probablePrime(int nBit) {
		BigNum result = genRandomOdd(nBit);
		do {
			result = result.add(TWO);
		} while (!result.isPrimeMR(5));
		return result;
	}
	
	public boolean isOdd() {
		return (num[0] & 1) == 1;
	}
	
	public BigNum modPow(BigNum y, BigNum p) {
		BigNum result = new BigNum(new int[]{1}), x = new BigNum(this.num.clone());
		x = x.mod(p);
		while (y.compareTo(ZERO) > 0) {
			if (y.isOdd()) {
				result = result.multiply(x).mod(p);
			}
			y = y.shiftRight(1);
			x = x.multiply(x).mod(p);
		}
		return result;
	}
	
	public BigNum modInverse(BigNum m) {
		BigNum x1 = ONE, x0 = ZERO, a = this, q = m, r, h;
		boolean pos = false;
		while (a.compareTo(ZERO) > 0) {
			r = q.mod(a);
			q = q.divide(a);
			h = q.multiply(x1).add(x0);
			x0 = x1;
			x1 = h;
			q = a;
			a = r;
			pos = !pos;
		}
		BigNum result = pos ? x0 : m.subtract(x0);
		return result;
	}
	
	public BigNum shiftLeft(int n) {
		int[] res = this.num.clone();
		for (int a = 0; a < n; a++) {
			boolean carry = false;
			for (int i = res.length - 1; i >= 0; i--) {
				if ((res[i] & 0x80000000) == 0x80000000) {
					if (i == res.length - 1) {
						carry = true;
					}
					else {
						res[i + 1] |= 0x1;
					}
				}
				res[i] <<= 1;
			}
			if (carry) {
				int[] temp = res;
				res = new int[temp.length + 1];
				for (int i = 0; i < temp.length; i++) {
					res[i] = temp[i];
				}
				res[temp.length] = 0x1;
			}
		}
		BigNum result = new BigNum(res);
		return result;
	}
	
	public BigNum shiftRight(int n) {
		int[] res = this.num.clone();
		for (int a = 0; a < n; a++) {
			for (int i = 0; i < res.length - 1; i++) {
				res[i] >>>= 1;
				if ((res[i + 1] & 0x1) == 0x1) {
					res[i] |= 0x80000000;
				}
			}
			res[res.length - 1] >>>= 1;
			if (res[res.length - 1] == 0 && res.length > 1) {
				int[] temp = res;
				res = new int[temp.length - 1];
				for (int i = 0; i < res.length; i++) {
					res[i] = temp[i];
				}
			}
		}
		BigNum result = new BigNum(res);
		return result;
	}
	
	public int compareTo(BigNum bn) {
		long num1, num2;
		if (this.num.length > bn.num.length) return 1;
		else if (this.num.length < bn.num.length) return -1;
		else {
			for (int i = this.num.length - 1; i >= 0; i--) {
				num1 = this.num[i];
				num1 = (num1 << 0x20) >>> 0x20;
				num2 = bn.num[i];
				num2 = (num2 << 0x20) >>> 0x20;
				if (num1 > num2) return 1;
				else if (num1 < num2) return -1;
			}
			return 0;
		}
	}
	
	public BigNum not() {
		int[] res = this.num.clone();
		for (int i = 0; i < res.length; i++) {
			res[i] = ~res[i];
		}
		BigNum result = new BigNum(res);
		return result;
	}
	
	public BigNum and(BigNum bn) {
		int[] res = (this.num.length < bn.num.length) ? this.num.clone() : bn.num.clone();
		int smallerLength = (this.num.length < bn.num.length) ? this.num.length : bn.num.length;
		for (int i = 0; i < smallerLength; i++) {
			res[i] = this.num[i] & bn.num[i];
		}
		BigNum result = new BigNum(res);
		return result;
	}
	
	public BigNum or(BigNum bn) {
		int[] res = (this.num.length > bn.num.length) ? this.num.clone() : bn.num.clone();
		int smallerLength = (this.num.length < bn.num.length) ? this.num.length : bn.num.length;
		for (int i = 0; i < smallerLength; i++) {
			res[i] = this.num[i] | bn.num[i];
		}
		BigNum result = new BigNum(res);
		return result;
	}
	
	public boolean equals(BigNum bn) {
		return compareTo(bn) == 0;
	}
	
	public void print() {
		for (int i = num.length - 1; i >= 0; i--) {
			System.out.print(Integer.toUnsignedString(num[i]));
		}
		System.out.println();
	}
	
	public void printHex() {
		for (int i = num.length - 1; i >= 0; i--) {
			if (i == num.length - 1) {
				System.out.print(String.format("0x%08X", num[i]));
			}
			else {
				System.out.print(String.format("%08X", num[i]));
			}
		}
		System.out.println();
	}
	
	public int[] toIntArray() {
		return num;
	}

}
