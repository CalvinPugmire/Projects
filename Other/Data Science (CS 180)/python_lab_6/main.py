import argparse


def main(number):
    # Write the code to determine whether or not a number is a pallindrome here.
    # Make sure that your terminal output matches the terminal output of the example given on the instructions.

    num = str(number)

    for i in range(len(num)//2):
        if num[i] != num[len(num)-i-1]:
            print("False")
            return False

    print("True")
    return True


if __name__ == "__main__":
    arg = argparse.ArgumentParser("Pallindrome Checker")
    arg.add_argument("--x", type=int, help="Input a number to determine if it's a pallindrome")
    parsed = arg.parse_args()
    main(parsed.x)
