def divmod10(x,y):
	x = str(x)
	y = str(y)
	return int(x[:len(x)-len(y)+1]),int(x[-len(y)+1:])

# global ten_pow
ten_pow = {}
for i in range(1000):
	if i==0:
		ten_pow[i]=1
	else:
		ten_pow[i]=ten_pow[i-1]*10

"""
	Binary Expo
"""
def pow(x,y):
	result=1;
	while (y!=0):
		if (y&1==1):
			result*=x
		y>>=1
		x*=x
	# print result
	return result;

"""
	Karatsuba Multiplication
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
		z1 = mul(x1+x0,y1+y0)
		z2 = mul(x1,y1)

		"""
			Result
		"""
		# print z2, z1, z0
		result = z2 * ten_pow[2*m] + ((z1-z2-z0) * ten_pow[m]) + z0
		return result

x = 102131243242432123627183298732895738459465
y = 124323123443243238764283289832740832702837
print mul(x,y)
# print pow(3,200)
# print divmod10(144,100)