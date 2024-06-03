import json
import string
import argparse
import os


def main(inputstring):
    # Write the code to count the number of words here
    # Remember to save the dictionary as a json file named "word-counts.json"

    lower = inputstring.lower()
    nopunct = lower.translate(str.maketrans('', '', string.punctuation))
    split = nopunct.split()

    wordcount = {}
    while len(split) > 0:
        word = split[0]
        count = split.count(word)
        wordcount[word] = count
        for i in range(count):
            split.remove(word)

    word_counts = json.dumps(wordcount, indent=0)
    with open("word-counts.json", "w") as outfile:
        outfile.write(word_counts)

    return None


if __name__ == "__main__":
    parser = argparse.ArgumentParser("Word Counter")
    parser.add_argument("-s", "--string", type=str, required=True, help="Sentence to have the number of words counted")
    args = parser.parse_args()
    main(args.string)
