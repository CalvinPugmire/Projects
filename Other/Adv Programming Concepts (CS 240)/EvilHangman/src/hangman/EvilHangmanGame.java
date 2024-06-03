package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    private Set<String> wordSet;
    private SortedSet<Character> guesses;
    private String word;

    public EvilHangmanGame() {
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        wordSet = new TreeSet<>();
        try (Scanner scanner = new Scanner(dictionary)) {
            while (scanner.hasNext()) {
                if (scanner.hasNext("[A-Za-z]+")) {
                    String inputText = scanner.next();
                    inputText = inputText.toLowerCase();
                    wordSet.add(inputText);
                } else {
                    scanner.next();
                }
            }
        } catch (IOException i) {
            throw new IOException();
        }
        if (wordSet.size() == 0) {
            throw new EmptyDictionaryException();
        }
        Set<String> dictionaryMatches = new TreeSet<>();
        for (String entry : wordSet) {
            if (entry.length() == wordLength) {
                dictionaryMatches.add(entry);
            }
        }
        if (dictionaryMatches.size() == 0) {
            throw new EmptyDictionaryException();
        }
        wordSet = dictionaryMatches;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            builder.append("_");
        }
        word = builder.toString();

        guesses = new TreeSet<>();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        String guessS = String.valueOf(guess);
        guessS = guessS.toLowerCase();
        guess = guessS.charAt(0);

        for (char guessC : guesses) {
            if (guess == guessC) {
                throw new GuessAlreadyMadeException();
            }
        }
        guesses.add(guess);

        Map<String, Set<String>> partitions = new TreeMap<>();
        for (String entry : wordSet) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < entry.length(); i++) {
                if (entry.charAt(i) == guess) {
                    builder.append(guess);
                } else {
                    builder.append("_");
                }
            }
            String key = builder.toString();
            if (partitions.containsKey(key)) {
                partitions.get(key).add(entry);
            } else {
                Set<String> values = new TreeSet<>();
                values.add(entry);
                partitions.put(key,values);
            }
        }

        int max = 0;
        String key = "";
        guessS = String.valueOf(guess);
        Set<String> newWordSet = new TreeSet<>();
        for (String partition : partitions.keySet()) {
            if (partitions.get(partition).size() > max) {
                max = partitions.get(partition).size();
                key = partition;
                newWordSet = partitions.get(partition);
            } else if (partitions.get(partition).size() == max) {
                if (!partition.contains(guessS)) {
                    max = partitions.get(partition).size();
                    key = partition;
                    newWordSet = partitions.get(partition);
                } else {
                    int count1 = 0;
                    for(int i=0; i < partition.length(); i++)
                    {    if(partition.charAt(i) == guess)
                        count1++;
                    }
                    int count2 = 0;
                    for(int i=0; i < key.length(); i++)
                    {    if(key.charAt(i) == guess)
                        count2++;
                    }
                    if (count1 < count2) {
                        max = partitions.get(partition).size();
                        key = partition;
                        newWordSet = partitions.get(partition);
                    } else if (key.contains(guessS) && count1 == count2){
                        boolean different = false;
                        int index = 0;
                        while (!different) {
                            if (partition.indexOf(guessS, index) > key.indexOf(guessS, index)) {
                                max = partitions.get(partition).size();
                                key = partition;
                                newWordSet = partitions.get(partition);
                                different = true;
                            } else if (partition.indexOf(guessS, index) < key.indexOf(guessS, index)) {
                                different = true;
                            } else {
                                index = partition.indexOf(guessS, index+1);
                            }
                        }
                    }
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == '_' && key.charAt(i) == guess) {
                builder.append(guess);
            } else {
                builder.append(word.charAt(i));
            }
        }
        word = builder.toString();

        wordSet = newWordSet;
        return wordSet;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guesses;
    }

    public String getWord() {
        return word;
    }
}
