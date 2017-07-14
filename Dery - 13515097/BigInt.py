"""
	Name : Dery Rahman Ahaddienata
	Nim  : 13515097

	BigInt lib. All operate with string manipulation
"""


"""
	Generate ten power of n (n is integer above 0).
	This can optimize karatsuba multiplication
"""
ten_pow = {}
for i in range(10000):
	if i==0:
		ten_pow[i]=1
	else:
		ten_pow[i]=ten_pow[i-1]*10

"""
	BigInt Class
"""
class BigInt:

	"""
		Constructor
	"""
	def __init__(self, x):
		self.num = ""
		for i in range(len(x)):
			if(x[i]=='-'):
				self.num = '-'
				continue
			if(x[i]=='0'): continue
			self.num += x[i:]
			break
		if(len(self.num)==0 or self.num=="-"):
			self.num = "0"

	"""
		Print BigInt
	"""
	def __str__(self):
		return self.num

	"""
		Addition Operator
	"""
	def __add__(self,num2):
		y = str(num2.num)
		x = str(self.num)

		# Case when negative
		if(x[0]=='-' and y[0]=='-'):
			return BigInt('-'+(BigInt(x[1:])+BigInt(y[1:])).num)
		if(x[0]=='-'):
			return BigInt(y)-BigInt(x[1:])
		if(y[0]=='-'):
			return BigInt(x)-BigInt(y[1:])

		# Length of string x must greather than y
		if len(x)<len(y):
			temp = x
			x = y
			y = temp
		max = len(x)

		# Zero leading string for string y
		for i in range(max-len(y)):
			y = "0"+y

		# Initial
		reminder = 0
		result = ""

		# Addition operation :)
		for i in range(max):
			curr = int(x[len(x)-1-i])+int(y[len(y)-1-i])+reminder
			reminder = curr/10
			curr = curr%10
			result = str(curr)+result
		result = str(reminder) + result
		return BigInt(result)

	"""
		Substraction Operator
	"""
	def __sub__(self,num2):
		y = str(num2.num)
		x = str(self.num)
		negative = False

		# Case when negative
		if(x[0]=='-' and y[0]=='-'):
			# -x+y
			return BigInt(x[1:])-BigInt(y[1:])
		if(x[0]=='-'):
			# -x-y
			return BigInt('-'+(BigInt(x[1:])+BigInt(y)).num)
		if(y[0]=='-'):
			# x+y
			return BigInt(x)+BigInt(y[1:])

		# If self<num2 revert and put sign '-' on front of the result
		if self<num2:
			negative = True
			temp = x
			x = y
			y = temp
		max = len(x)

		# Zero leading string for string y
		for i in range(max-len(y)):
			y = "0"+y

		# Initial
		reminder = 0
		result = ""

		# Substraction operation :)
		for i in range(max):
			curr = int(x[len(x)-1-i])-int(y[len(y)-1-i])-reminder
			if curr<0:
				reminder = 1
				curr = 10+curr
			else:
				reminder = 0
			result = str(curr)+result

		# Add negative sign when self>num2
		if negative:
			return BigInt('-'+result)

		return BigInt(result)

	"""
		Div and mod for small portion of string number
		This can optimize div mod operator
	"""
	def divmod_each(self,y):
		x = self
		q = BigInt("0")

		# Calculate with substraction
		while x>=y:
			q+=BigInt("1")
			x-=y
		x = x.num
		q = q.num
		if x=="0":
			x = ""
		return q,x

	"""
		Calculate div and mod, even for large number using long division, porogapit in Indonesia :)
	"""
	def divmod(self,y):
		x = str(self.num)
		y = str(y.num)

		# Case when negative
		if(x[0]=='-' and y[0]=='-'):
			return BigInt(x[1:])/BigInt(y[1:]),BigInt('-'+(BigInt(x[1:])%BigInt(y[1:])).num)
		if(x[0]=='-'):
			s = ((BigInt(x[1:])/BigInt(y))+BigInt("1")).num
			return BigInt('-'+s), BigInt(s)*BigInt(y)+BigInt(x)
		if(y[0]=='-'):
			s = ((BigInt(x)/BigInt(y[1:]))+BigInt("1")).num
			return BigInt('-'+s), BigInt(s)*BigInt(y)+BigInt(x)

		# Initial
		q = ""
		j = len(x)

		# Division operation :)
		while BigInt(x)>=BigInt(y) or j>0:
			j-=1
			while True:
				if j==0:
					x_head, x_tail = x, ""
				else:
					x_head, x_tail = x[:-j], x[-j:]
				if(BigInt(x_head)>=BigInt(y)) or j<=0:
					break
				j-=1
				q = q+"0"
			q_temp,x_add = BigInt(x_head).divmod_each(BigInt(y))
			x = x_add + x_tail
			q = q + q_temp
		return BigInt(q),BigInt(x)

	"""
		Division and Modulo with base10
	"""
	def divmod10(self,exp):
		x = str(self.num)
		y = str(exp)

		# Div mod when the divisor is base10
		if(len(y)>len(x)):
			return BigInt("0"),BigInt(x)
		return BigInt(x[:len(x)-len(y)+1]),BigInt(x[-len(y)+1:])

	"""
		Division and Modulo operator
	"""
	def __div__(self,y):
		res, dummy = self.divmod(y)
		return res
	def __mod__(self,y):
		dummy, res = self.divmod(y)
		return res

	"""
		Multiplication with base10
	"""
	def mul10(self,x,y):
		x = str(x.num)
		x += str(y)[1:]
		return BigInt(x)

	"""
		Karatsuba Multiplication with base10
	"""
	def __mul__(self,num2):
		x = str(self.num)
		y = str(num2.num)

		# Case when negative
		if(x[0]=='-' and y[0]=='-'):
			return BigInt(x[1:])*BigInt(y[1:])
		if(x[0]=='-'):
			return BigInt('-'+(BigInt(x[1:])*BigInt(y)).num)
		if(y[0]=='-'):
			return BigInt('-'+(BigInt(y[1:])*BigInt(x)).num)

		# Base case
		if (len(x)==1 or len(y)==1):
			return BigInt(str(int(x)*int(y)))
		else:
			n = max(len(x),len(y))
			m = n/2

			"""
				Split number into form, where B=10
					x = x1.B^m + x0
					y = y1.B^m + y0
			"""
			x1,x0 = self.divmod10(ten_pow[m]) # x/B**m,x%B**m
			y1,y0 = num2.divmod10(ten_pow[m]) # y/B**m,y%B**m

			"""
				Compute three multiplications, with recursive
			"""
			z0 = x0*y0
			z1 = (x1+x0)*(y1+y0)
			z2 = x1*y1

			"""
				Result
			"""
			result = self.mul10(z2,ten_pow[2*m]) + self.mul10(z1-(z2+z0),ten_pow[m]) + z0 # z2*(B**(2*m))+(z1-z2-z0)*(B**m)+z0
			return result

	"""
		Power Algorithm
	"""
	def __pow__(self,y):

		result = BigInt("1")
		while (y > BigInt("0")):
			if (y%BigInt("2") == BigInt("1")):
				result = (result * self)
			self = (self * self)
			y = y/BigInt("2")
		return result

	"""
		Fast power mod algorithm
	"""
	def pow_mod(self,y,m):
		if (self<BigInt("1") or y<BigInt("0") or m<BigInt("1")):
			return -1
		y=y%m
		result = BigInt("1")
		while (y > BigInt("0")):
			if (y%BigInt("2") == BigInt("1")):
				result = (result * self) % m
			self = (self * self) % m
			y = y/BigInt("2")
		return result % m

	"""
		Comparation
		equal = 0
		greater than = 1
		less than = 2
	"""
	def cmp(self,y):
		x = str(self.num)
		y = str(y.num)

		if(len(x)>len(y)):
			return 1
		if(len(x)<len(y)):
			return 2

		for i in range(len(x)):
			if(x[i]>y[i]):
				return 1
			if(x[i]<y[i]):
				return 2
		return 0

	"""
		Comparation operator
	"""
	def __eq__(self,num2):
		return self.cmp(num2)==0
	def __ne__(self,num2):
		return self.cmp(num2)!=0
	def __gt__(self,num2):
		return self.cmp(num2)==1
	def __ge__(self,num2):
		return self.cmp(num2)==1 or self.cmp(num2)==0
	def __lt__(self,num2):
		return self.cmp(num2)==2
	def __le__(self,num2):
		return self.cmp(num2)==2 or self.cmp(num2)==0