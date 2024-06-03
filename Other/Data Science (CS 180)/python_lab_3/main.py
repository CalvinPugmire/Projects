import argparse
import numpy
import os
import math


def main(number: int) -> int:
    # Write the code to sum up cubed numbers here.
    # Make sure that your terminal output matches the terminal output of the example given on the instructions.

    result = 0

    for i in range(1, number+1):
        num = i**3
        first = int(num / pow(10, int(math.log10(num))))
        if first % 2 == 0:
            result += num

    print('cube(' + str(number) + ') = ' + str(result))

    return result


if __name__ == "__main__":
    parser = argparse.ArgumentParser("Cube Counter")
    parser.add_argument("--n", type=int, required=True, help="Input a number to sum the cube counts")
    arguments = parser.parse_args()
    main(arguments.n)
