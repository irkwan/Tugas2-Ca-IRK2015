def divmod(x,y):
	q=0
	while x>=y:
		q+=1
		x-=y
	return q,x

"""
	Binary Exponential
"""
MOD = 1000000007
def pow(x,y):
	result=1
	while y>0:
		# If power is odd
		if y%2==1:
			result=(mul(result,x))%MOD
			# Divide the power by 2
			y = y/2
			# Multiply base to itself
			x = (mul(x,x))%MOD
	return result

"""
	Karatsuba Multiplication
"""
def mul(x,y):
	if (len(str(x))<19 or len(str(y))<19): # if length of number below length of 2^64-1 bit, then multiply like a normal
		return x*y
	else:
		n = max(len(str(x)),len(str(y)))
		m = n/2
		B = 10

		"""
			Split number into form
				x = x1.B^m + x0
				y = y1.B^m + y0
		"""
		x1,x0 = divmod(x,pow(B,m))
		y1,y0 = divmod(y,pow(B,m))

		# Compute three multiplications, with recursive
		z0 = mul(x0,y0)
		z1 = mul(x1+x0,y1+y0)
		z2 = mul(x1,y1)

		# Result
		result = mul(z2,pow(B,mul(2,m))) + mul((z1-z2-z0),pow(B,m)) + z0
		return result

x = 102131243242432
y = 1243231234432432
print mul(x,y)
# print divmod(14,3)