import argparse
import numpy as np


def main(documentsTxt):
    # Write the code to compute the One Hot Encodings for various "documents"
    # Make sure that your terminal output matches the terminal output of the example given on the instructions.

    lower = documentsTxt.lower()
    split = lower.split()
    documents = lower.split('\n')

    words = []
    while len(split) > 0:
        word = split[0]
        count = split.count(word)
        words.append(word)
        for i in range(count):
            split.remove(word)
    words.sort()

    features = np.zeros((len(documents), len(words)))
    for i in range(len(documents)):
        document = documents[i].split()
        for j in range(len(words)):
            features[i][j] = int(document.count(words[j]))

    print("# Features:")
    print(features.astype(int))

    return None


if __name__ == "__main__":
    parser = argparse.ArgumentParser("One Hot Encoder")
    parser.add_argument("--fpath", type=str, help="Name of the txt file to be read in")
    args = parser.parse_args()
    main(open(args.fpath).read())