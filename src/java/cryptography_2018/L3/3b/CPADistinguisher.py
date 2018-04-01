
import base64, jks, pyaes, random, struct, sys, textwrap

# We shall assume that IV is incremented
AliceIV = ''
BobIV = ''

def bin_add(*args): 
	return bin(sum(int(x, 2) for x in args))[2:]

def getAliceIV(counter, AliceIV):
	if (counter == 0): 
		return AliceIV
	else:
		AliceIV = bin_add(AliceIV, '1')
		if (len(AliceIV) < 16):
			for i in range(16-len(AliceIV)):
				AliceIV = '0' + AliceIV
		return AliceIV

# def getBobIV(counter, BobIV):
# 	if (counter == 0):
# 		return BobIV
# 	else:
# 		BobIV = bin_add(BobIV, '1')
# 		if (len(BobIV) < 16):
# 			for i in range (16 - len(BobIV)):
# 				BobIV = '0' + BobIV
# 		return BobIV

def guessMessage(key, bobsStatement, receivedCiphertext):
	AliceIV = receivedCiphertext[:16]
	aes = pyaes.AESModeOfOperationCBC(key, AliceIV)
	# ciphertext should be equal to Enc(AliceIV XOR 'false')
	# see https://stackoverflow.com/questions/3008139/why-is-using-a-non-random-iv-with-cbc-mode-a-vulnerability
	ciphertext = aes.encrypt(xor_bytes(AliceIV, bobsStatement))

	if (ciphertext == receivedCiphertext):
		b = 0
	else:
		b = 1

	return b

def CPADistinguisher(keytool):
	key = readKeystore(keytool)
	aes = pyaes.AESModeOfOperationCBC(key)
	# get Bob's message
	bobsStatement = '0' * 16
	# get a random message from Alice
	alicesStatement = ''
	for i in range(16):
		alicesStatement = alicesStatement + str(random.randint(0, 9))
	# return ciphertext of randomly chosen message
	chosenStatement = random.SystemRandom().choice([alicesStatement, bobsStatement])
	print("Randomly chosen statement: %s" % chosenStatement)
	receivedCiphertext = aes.encrypt(chosenStatement)
	# obtain AliceIV 
	AliceIV = receivedCiphertext[:16]
	print(AliceIV)
	AliceIV = struct.unpack(AliceIV)[0]
	print(AliceIV)
	# predict next AliceIV value
	AliceIV = getAliceIV(AliceIV, 1)
	# BobIV is arbitrary, we can assume it's known
	BobIV = '0' * 15 + '1'
	# new AES instance for new encryption (with predicted AliceIV)
	aes2 = pyaes.AESModeOfOperationCBC(key, AliceIV)
	# compose next commitment: AliceIV XOR BobIV XOR '0' * 16
	bobsPlaintext = xor_bytes(xor_bytes(AliceIV, BobIV), bobsStatement)
	# get another random message from Alice
	alicesStatement = ''
	for i in range(16):
		alicesStatement = alicesStatement + str(random.randint(0, 9))
	# return ciphertext of randomly chosen message
	newChosenStatement = random.SystemRandom().choice([alicesStatement, bobsPlaintext])
	print("Randomly chosen statement: %s" % chosenStatement)
	receivedCiphertext = aes2.encrypt(chosenStatement)
	# let Bob guess
	bobsGuess = guessMessage(key, bobsStatement, newChosenStatement)

	if (bobsGuess == 0 and chosenStatement == bobsStatement):
		print("Bob guessed successfully! \n")
	elif (bobsGuess == 1 and chosenStatement == alicesStatement):
		print("Bob guessed successfully! \n")
	else:
		print("Bob made a mistake :( \n")



def readKeystore(keytool):
	ks = jks.KeyStore.load(keytool, "keystore")
	for alias, pk in ks.private_keys.items():
		print("Private key: %s" % "\r\n".join(textwrap.wrap(base64.b64encode(pk.pkey_pkcs8[:16]).decode('ascii'), 64)))

	return pk.pkey_pkcs8[:16]

def xor(a, b):
	return '0' if (a == b) else '1'

def xor_bytes(b1, b2):
	return ''.join([xor(a, b) for (a, b) in zip(b1, b2)])

CPADistinguisher("keystore.jks")