# RSA and Big Integer Implementation

## Overview
A simple RSA that can encrypt plaintext into ciphertext and decrypt it back to retrieve the text. Written in C++ languange, this project consists of **Big Integer Library and RSA**. The Big Integer Library can be used to create and operate very large integers while the RSA Library uses Big Integer Library to encrypt and decrypt text.

This project consists of two applications, the *Command Line Interface (CLI) app* and the *Graphical User Interface (GUI) app*. Both execute exactly same logic, however the GUI app has better interface than CLI app. This readme will explains how to run both applications.

## RSA With GUI
### Run
To run the application, download and unzip [MainGUI.zip](https://github.com/wijayaerick/Tugas2-Ca-IRK2015/blob/master/Erick%20Wijaya%20-%2013515057/MainGUI.zip) then open RSA-GUI.exe. Please note that currently the GUI application only works in Windows. If you have different OS, you can read the CLI documentation below the tutorial.

### Tutorial
These are steps to use the RSA with GUI app.
1. Browse file that you want to encrypt.

<img src="screenshot/gui1.png" width="350"> <img src="screenshot/gui2.png" width="400">

2. Generate the public and private key.

<img src="screenshot/gui3.png" width="400"> <img src="screenshot/gui4.png" width="400">

3. Encrypt the plaintext to ciphertext.

<img src="screenshot/gui5.png" width="400"> <img src="screenshot/gui6.png" width="400">

4. Decrypt the ciphertext back to plaintext.

<img src="screenshot/gui7.png" width="400"> <img src="screenshot/gui8.png" width="400">

5. You can reset the application by tab "Reset".

<img src="screenshot/gui9.png" width="400">

## RSA With CLI
### Run
To run the application, you will need an input file (text file) that contains message needed to be encrypted. Run the cmd or shell and type the command below.

```
./main <your-file.txt>
```
If you are using windows, you may need to use "main" instead of "./main".

### Compile
To compile the code, use this command below.
```
g++ src/biginteger.cpp src/rsa.cpp main.cpp -o main
```

### Output
![Input 3](screenshot/input3.PNG)


## Explanation
The Big Integer class uses std::vector to store the digits of integer. The Big Integer class has methods that overload c++ operators like operator +, -, /, %, ++, +=, and so on. Karatsuba Multiplication Algorithm is used to implement multiplication operator. Random probable prime numbers are generated with Miller-Rabin Primality Test Algorithm.

The RSA class has two primary methods and a constructor. The constructor handles generating the public and private key. First, the constructor generates two random probable prime numbers (p and q). The prime numbers are used to calculate n = p * q. After that, the euler phi function is calculated with formula eulerphi = (p - 1) * (q - 1). After that, pick a number e that is coprime with the eulerphi. The number e is the public key. To generate the private key d, calculate with e^-1 mod eulerphi (this can be done with Extended Euclidean Algorithm implemented in gcdExtended method). In this state the program have generated two keys, the public key e and the private key d. 

To encrypt a plaintext, the plaintext is converted into its ASCII values. Each values (ci) will be encrypted to create values for ciphertext (di). For each ci, calculate di = ci^e mod n. All the di values combined is the ciphertext. To decrypt the ciphertext back to plaintext, calculate ci = di^d mod n. All the ci values are converted back from ASCII to characters. To make the encryption and decryption safer, ci must be multiplied by random multiple of 256 before calculating di. After the decryption, the results must be divided by 256 to get the remainders. The remaindes are the ASCII values that are converted back to plaintext.

Please note that this program is created for educational purpose and not supposed to be used in practical usage.


## Documentation
See the [cppdoc documentation](https://github.com/wijayaerick/Tugas2-Ca-IRK2015/tree/master/Erick%20Wijaya%20-%2013515057/cppdoc) for more information about the classes and their methods.

