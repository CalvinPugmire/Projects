def reverse(forward):
    backward = ""
    for i in reversed(range(len(forward))):
        backward += forward[i]
    return backward


def largest(int1, int2, int3):
    if int1 >= int2 and int1 >= int3:
        return int1
    elif int2 >= int1 and int2 >= int3:
        return int2
    else:
        return int3


def factorial(n):
    return factorialhelper(n, n)


def factorialhelper(nfactorial, n):
    if n == 1:
        return nfactorial
    else:
        nfactorial = nfactorial*(n-1)
        return factorialhelper(nfactorial, n-1)


def fibonacci(n):
    if n == 1:
        return n
    nfib = [0 for _ in range(n)]
    nfib[0] = 1
    nfib[1] = 1
    for i in range(2, n):
        nfib[i] = nfib[i-1]+nfib[i-2]
    return nfib[n-1]


if __name__ == '__main__':
    while 1:
        print("Function 1: reverse of a string.")
        print("Function 2: greatest of three integers.")
        print("Function 3: factorial of an integer.")
        print("Function 4: Nth entry of the Fibonacci sequence.")
        print("Function 5: exit program.")
        option = int(input("Enter function number: "))

        if option == 1:
            forwardmain = input("Enter a word: ")
            backwardmain = reverse(forwardmain)
            print(backwardmain)

        elif option == 2:
            int1main = int(input("Enter first number: "))
            int2main = int(input("Enter second number: "))
            int3main = int(input("Enter third number: "))
            largestmain = largest(int1main, int2main, int3main)
            print(largestmain)

        elif option == 3:
            nmain = int(input("Enter factorial number: "))
            if nmain >= 1:
                nfactorialmain = factorial(nmain)
                print(nfactorialmain)
            else:
                print("Invalid input.")

        elif option == 4:
            nmain = int(input("Enter entry number: "))
            if nmain >= 1:
                nfibmain = fibonacci(nmain)
                print(nfibmain)
            else:
                print("Invalid input.")

        elif option == 5:
            exit(0)

        else:
            print("Invalid input.")
