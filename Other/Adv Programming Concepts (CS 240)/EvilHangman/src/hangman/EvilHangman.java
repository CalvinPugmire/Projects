package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangman {

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            File dictionaryFileName = new File(args[0]);
            int wordLength = Integer.parseInt(args[1]);
            int guesses = Integer.parseInt(args[2]);

            EvilHangmanGame game = new EvilHangmanGame();
            game.startGame(dictionaryFileName, wordLength);

            int i = 0;
            boolean win = false;
            boolean lose = false;
            Set<String> wordList = new TreeSet<>();
            while (!lose && !win) {
                System.out.println("You have " + (guesses - i) + " guesses left.");
                System.out.print("Used letters:");
                for (Character letter : game.getGuessedLetters()) {
                    System.out.print(" " + letter);
                }
                System.out.print("\n");
                System.out.println("Word: " + game.getWord());
                char guessC = 'a';
                boolean validGuess = false;
                while (!validGuess) {
                    System.out.print("Enter guess: ");
                    String guessS = in.nextLine();
                    guessS = guessS.toLowerCase();
                    if (guessS.length() != 1) {
                        System.out.println("Invalid input!");
                    } else {
                        guessC = guessS.charAt(0);
                        if (!Character.isAlphabetic(guessC)) {
                            System.out.println("Invalid input!");
                        } else {
                            boolean doubleguess = false;
                            try {
                                wordList = game.makeGuess(guessC);
                            } catch (GuessAlreadyMadeException g) {
                                doubleguess = true;
                                System.out.println("You already used that letter!");
                            }
                            if (!doubleguess) {
                                validGuess = true;
                            }
                        }
                    }
                }
                int count = 0;
                for (int j = 0; j < game.getWord().length(); j++) {
                    if (game.getWord().charAt(j) == guessC) {
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("Sorry, there are no " + guessC + "'s.");
                    i++;
                } else if (count == 1) {
                    System.out.println("Yes, there is " + count + " " + guessC + ".");
                } else {
                    System.out.println("Yes, there are " + count + " " + guessC + "'s.");
                }
                if (!game.getWord().contains("_")) {
                    win = true;
                }
                if (i == guesses) {
                    lose = true;
                }
                System.out.println();
            }
            if (win) {
                System.out.println("You win!");
                System.out.println("The word was: " + game.getWord());
            }
            if (lose) {
                String[] loseList = wordList.toArray(new String[0]);
                System.out.println("You lose!");
                System.out.println("The word was: " + loseList[0]);
            }
        } catch (IOException i) {
            System.out.println("IOException: " + i);
        } catch (EmptyDictionaryException e) {
            System.out.println("EmptyDictionaryException: " + e);
        }
    }

}
