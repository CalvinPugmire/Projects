import argparse as ap


def main(array):
    # Write the compute the variance and the mean of a given list of numbers
    # Make sure that your terminal output matches the terminal output of the example given on the instructions.

    mean = 0
    for i in range(len(array)):
        mean += array[i]
    mean = mean/len(array)
    print("mean =", mean)

    variance = 0
    for i in range(len(array)):
        variance += (array[i] - mean)**2
    variance = variance/len(array)
    print("variance =", variance)

    return None


if __name__ == "__main__":
    argParse = ap.ArgumentParser("Variance and Mean Calculator")
    argParse.add_argument("--array", nargs="+", type=int, help="Input a list of numbers to compute the variance and mean of")
    parsedArgs = argParse.parse_args()
    main(parsedArgs.array)