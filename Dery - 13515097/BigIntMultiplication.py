"""
	Addition Operator
"""
def add(x,y):
	x = str(x)
	y = str(y)

	if len(x)<len(y):
		temp = x
		x = y
		y = temp
	min = len(y)
	reminder = 0
	result = " "
	for i in range(min):
		curr = int(x[len(x)-1-i])+int(y[len(y)-1-i])+reminder
		reminder = curr/10
		curr = curr%10
		result = str(curr)+result

	x = list(x)
	x[len(x)-len(y)-1] = str(int(x[len(x)-len(y)-1])+reminder)
	x="".join(x)
	result = x[:len(x)-len(y)] + result
	return result

"""
	Minus Operator
"""
def min(x,y):
	x = str(x)
	y = str(y)
	if len(x)<len(y):
		temp = x
		x = y
		y = temp
	min = len(y)
	reminder = 0
	result = " "
	for i in range(min):
		curr = int(x[len(x)-1-i])+int(y[len(y)-1-i])-reminder
		if curr<0:
			reminder = 1
			curr = 10+curr
		else:
			reminder = 0
		result = str(curr)+result

	x = list(x)
	x[len(x)-len(y)-1] = str(int(x[len(x)-len(y)-1])-reminder)
	x="".join(x)
	result = x[:len(x)-len(y)] + result
	return result

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
			dummy,result=divmod((mul(result,x)),MOD)
			# Divide the power by 2
			y,dummy = divmod(y,2)
			# Multiply base to itself
			dummy,x = divmod((mul(x,x)),MOD)
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
		z1 = mul(add(x1,x0),add(y1,y0))
		z2 = mul(x1,y1)

		# Result
		result = add(mul(z2,pow(B,mul(2,m))),(add(mul((min(z1,min(z2,z0))),pow(B,m)),z0)))
		return result

x = 102131243242432
y = 1243231234432432
print mul(x,y)
# print divmod(14,3)