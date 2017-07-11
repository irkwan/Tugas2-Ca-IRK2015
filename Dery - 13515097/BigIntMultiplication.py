"""
	Karatsuba Multiplication
"""
def mul(x,y):
	if (len(str(x))==1 or len(str(y))==1):
		return x*y
	else:
		n = max(len(str(x)),len(str(y)))
		m = n / 2
		B = 10

		"""
			Split number into form
				x = x1.B^m + x0
				y = y1.B^m + y0
		"""
		x1 = x / B**(m)
		x0 = x % B**(m)
		y1 = y / B**(m)
		y0 = y % B**(m)

		"""
			Compute three multiplications, with recursive
		"""
		z0 = mul(x0,y0)
		z1 = mul(x1+x0,y1+y0)
		z2 = mul(x1,y1)

		"""
			Result
		"""
		result = z2 * B**(2*m) + ((z1-z2-z0) * B**m) + z0
		return result

x = 1324354647489202004938573894030298473858930827492
y = 7384723943283474329472364938761038746104361086412634
print mul(x,y)