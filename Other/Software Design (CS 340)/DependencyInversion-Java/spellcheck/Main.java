
package spellcheck;

import java.io.IOException;
import java.net.URL;
import java.util.SortedMap;


public class Main {
	public static void main(String[] args) {
		try {
			URL url = new URL(args[0]);
			URLFetcher fetcher = new URLFetcher();
			WordExtractor extractor = new WordExtractor();
			TxtDictionary txtDictionary = new TxtDictionary("dict.txt");

			SpellingChecker checker = new SpellingChecker(fetcher, extractor, txtDictionary);
			SortedMap<String, Integer> mistakes = checker.check(url);
			System.out.println(mistakes);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

