# class TenPow:
# 	@classmethod
# 	def ten_pow(i):
# 		return ten_pow[i]

ten_pow = {}
for i in range(10000):
	if i==0:
		ten_pow[i]=1
	else:
		ten_pow[i]=ten_pow[i-1]*10

class BigInt:
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

	def __str__(self):
		return self.num

	def ten(self,i):
		return self.ten_pow[i]

	def length(self):
		return len(self.num)

	def split_on(self,m):
		return self.num[:m], self.num[m:]

	def concate_front(self,num2):
		s = num2.num
		for i in range(len(s)):
			if(s[i]=='-'):
				self.num = '-'
				continue
			if(s[i]=='0'): continue
			self.num = s[i:]+self.num
			break
		# print self.num

	def concate_back(self,num2):
		self.num = self.num + num2.num

	def is_zero(self):
		return len(self.num)==0 or self.num=="0"
	"""
		Addition Operator
	"""
	def __add__(self,num2):
		y = num2.num
		x = self.num

		if(x[0]=='-' and y[0]=='-'):
			return BigInt('-'+(BigInt(x[1:])+BigInt(y[1:])).num)
		if(x[0]=='-'):
			return BigInt(y)-BigInt(x[1:])
		if(y[0]=='-'):
			return BigInt(x)-BigInt(y[1:])

		if len(x)<len(y):
			temp = x
			x = y
			y = temp
		max = len(x)
		for i in range(max-len(y)):
			y = "0"+y
		reminder = 0
		result = ""
		for i in range(max):
			curr = int(x[len(x)-1-i])+int(y[len(y)-1-i])+reminder
			reminder = curr/10
			curr = curr%10
			result = str(curr)+result
		result = str(reminder) + result
		return BigInt(result)

	"""
		Substraction Operator
		x always >= y
	"""
	def __sub__(self,num2):
		y = num2.num
		x = self.num

		if(x[0]=='-' and y[0]=='-'):
			# -x+y
			return BigInt(x[1:])-BigInt(y[1:])
		if(x[0]=='-'):
			# -x-y
			return BigInt('-'+(BigInt(x[1:])+BigInt(y)).num)
		if(y[0]=='-'):
			# x+y
			return BigInt(x)+BigInt(y[1:])

		negative = False
		if self<num2:
			negative = True
			temp = x
			x = y
			y = temp

		x = str(x)
		y = str(y)
		max = len(x)
		for i in range(max-len(y)):
			y = "0"+y
		# print x
		# print y
		reminder = 0
		result = ""
		for i in range(max):
			# print x[len(x)-1-i], y[len(x)-1-i]
			curr = int(x[len(x)-1-i])-int(y[len(y)-1-i])-reminder
			if curr<0:
				reminder = 1
				curr = 10+curr
			else:
				reminder = 0
			# print str(curr)+result
			result = str(curr)+result
		# print BigInt(result)
		if negative:
			return BigInt('-'+result)
		return BigInt(result)

	def divmod_each(self,y):
		x = self
		q=BigInt("0")
		while x>=y:
			q+=BigInt("1")
			x-=y
		x = x.num
		q = q.num
		# if q=="0":
		# 	q=""
		if x=="0":
			x = ""
		return q,x

	def divmod(self,y):
		x = self.num
		y = y.num

		if(x[0]=='-' and y[0]=='-'):
			return BigInt(x[1:])/BigInt(y[1:]),BigInt('-'+(BigInt(x[1:])%BigInt(y[1:])).num)
		if(x[0]=='-'):
			s = ((BigInt(x[1:])/BigInt(y))+BigInt("1")).num
			return BigInt('-'+s), BigInt(s)*BigInt(y)+BigInt(x)
		if(y[0]=='-'):
			s = ((BigInt(x)/BigInt(y[1:]))+BigInt("1")).num
			return BigInt('-'+s), BigInt(s)*BigInt(y)+BigInt(x)

		q = ""
		j = len(x)
		while BigInt(x)>=BigInt(y) or j>0:
			j-=1
			while True:
				if j==0:
					x_head, x_tail = x, ""
				else:
					x_head, x_tail = x[:-j], x[-j:]
				# print j
				# print x_head, x_tail
				if(BigInt(x_head)>=BigInt(y)) or j<=0:
					break
				j-=1
				q = q+"0"
			# print x_head, y
			q_temp,x_add = BigInt(x_head).divmod_each(BigInt(y))
			x = x_add + x_tail
			q = q + q_temp
			# print "Q", q
		return BigInt(q),BigInt(x)

	"""
		Division and Modulo with base10
	"""
	def divmod10(self,exp):
		x = self.num
		y = exp

		x = str(x)
		y = str(y)
		if(len(y)>len(x)):
			# print "BUM"
			return BigInt("0"),BigInt(x)
		# print BigInt(x[:len(x)-len(y)+1]), BigInt(x[-len(y)+1:])
		return BigInt(x[:len(x)-len(y)+1]),BigInt(x[-len(y)+1:])

	"""
		Multiplication with base10
		x = Number
		y = Number with base10 ex. 10000
	"""
	def mul10(self,x,y):
		x = x.num
		x = str(x)
		x += str(y)[1:]
		return BigInt(x)

	"""
		Power Algorithm
	"""
	def __pow__(self,y):
		result=BigInt("1")
		x = self
		y = (int)(y.num) # assume never exceed 2^64-1
		while(y!=0):
			if(y&1==1):
				result=result*x
			y>>=1
			x=x*x
		return result

	"""
		Karatsuba Multiplication with base10
	"""
	def __mul__(self,num2):
		x = self.num
		y = num2.num

		if(x[0]=='-' and y[0]=='-'):
			return BigInt(x[1:])*BigInt(y[1:])
		if(x[0]=='-'):
			return BigInt('-'+(BigInt(x[1:])*BigInt(y)).num)
		if(y[0]=='-'):
			return BigInt('-'+(BigInt(y[1:])*BigInt(x)).num)

		if (len(x)==1 or len(y)==1):
			# print type(BigInt(str(int(x)*int(y))))
			# print x, y
			# print BigInt(str(int(x)*int(y)))
			return BigInt(str(int(x)*int(y)))
		else:
			n = max(len(x),len(y))
			m = n/2
			# B = 10
			"""
				Split number into form
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
		Comparation
		equal 0
		greater than 1
		less than 2
	"""
	def cmp(self,y):
		x = self.num
		y = y.num

		x = str(x)
		y = str(y)

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

	def __div__(self,y):
		res, dummy = self.divmod(y)
		return res
	def __mod__(self,y):
		dummy, res = self.divmod(y)
		return res
# x = BigInt("930756436598645946732525352353252353223532596759372658362763298647268352")
# y = BigInt("33253253")
# print x-y
x = BigInt("14")
y = BigInt("-3")
# x = BigInt("3000")
# y = BigInt("3")
# print x/y, x%y
print x/y, x%y
# x = BigInt("100001")
# y = BigInt("3")
# print x/y, x%y
# print x.split_on(3)[1]

# z = "12345"
# print x.length()
# print x**y