"""
	Comparation
	equal 0
	greater than 1
	less than 2
"""
def cmp(x,y):
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
	max = len(x)
	for i in range(max-len(y)):
		y = "0"+y
	reminder = 0
	result = " "
	for i in range(max):
		curr = int(x[len(x)-1-i])+int(y[len(y)-1-i])+reminder
		reminder = curr/10
		curr = curr%10
		result = str(curr)+result
	result = str(reminder) + result
	return int(result)

"""
	Substraction Operator
	x always >= y
"""
def sub(x,y):
	# if(cmp(x,y)==2): # less than
	# 	temp = x
	# 	x = y
	# 	y = temp
	# print x, y
	x = str(x)
	y = str(y)
	max = len(x)
	for i in range(max-len(y)):
		y = "0"+y
	reminder = 0
	result = " "
	for i in range(max):
		# print x[len(x)-1-i], y[len(x)-1-i]
		curr = int(x[len(x)-1-i])-int(y[len(y)-1-i])-reminder
		if curr<0:
			reminder = 1
			curr = 10+curr
		else:
			reminder = 0
		result = str(curr)+result

	# x = list(x)
	# x[len(x)-len(y)-1] = str(int(x[len(x)-len(y)-1])-reminder)
	# x="".join(x)
	# result = x[:len(x)-len(y)] + result
	return int(result)


"""
	Division and Modulo with base10
"""
def divmod10(x,y):
	x = str(x)
	y = str(y)
	if(len(y)>len(x)):
		return 0,int(x)
	return int(x[:len(x)-len(y)+1]),int(x[-len(y)+1:])

"""
	Power with base10
"""
# global ten_pow
ten_pow = {}
for i in range(1000):
	if i==0:
		ten_pow[i]=1
	else:
		ten_pow[i]=ten_pow[i-1]*10

"""
	Multiplication with base10
	x = Number
	y = Number with base10 ex. 10000
"""
def mul10(x,y):
	x = str(x)
	x += str(y)[1:]
	return int(x)

"""
	Karatsuba Multiplication with base10
"""
def mul(x,y):
	if (len(str(x))==1 or len(str(y))==1):
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
		x1,x0 = divmod10(x,ten_pow[m]) # x/B**m,x%B**m
		y1,y0 = divmod10(y,ten_pow[m]) # y/B**m,y%B**m

		"""
			Compute three multiplications, with recursive
		"""
		z0 = mul(x0,y0)
		z1 = mul((x1+x0),(y1+y0))
		z2 = mul(x1,y1)

		"""
			Result
		"""
		result = add(mul10(z2,ten_pow[2*m]),add(mul10((sub(z1,add(z2,z0))),ten_pow[m]),z0)) # z2*(B**(2*m))+(z1-z2-z0)*(B**m)+z0
		return result

x = 102131243242432123627183298732895738436453563465346534634653456764567456747654354353453345635634654634653465454354559465
y = 124323123443243238764283289832740834653465436534653456346346327028345345334534545346534563463564576456745673465654453537
print mul(x,y)