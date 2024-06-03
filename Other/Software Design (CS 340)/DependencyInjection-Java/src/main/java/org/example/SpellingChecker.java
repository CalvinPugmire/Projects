
package org.example;

import com.google.inject.Inject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class SpellingChecker {
    private Fetcher fetcher;
    private Extractor extractor;
    private Dictionary dictionary;

    @Inject
    public SpellingChecker (Fetcher fetcher, Extractor extractor, Dictionary dictionary) {
        this.fetcher = fetcher;
        this.extractor = extractor;
        this.dictionary = dictionary;
    }

	public SortedMap<String, Integer> check(URL url) throws IOException {

		// download the document content
		//
		String content = fetcher.fetch(url);

		// extract words from the content
		//
		List<String> words = extractor.extract(content);

		// find spelling mistakes
		//
		SortedMap<String, Integer> mistakes = new TreeMap<>();

        for (String word : words) {
            if (!dictionary.isValidWord(word)) {
                if (mistakes.containsKey(word)) {
                    int oldCount = mistakes.get(word);
                    mistakes.put(word, oldCount + 1);
                } else {
                    mistakes.put(word, 1);
                }
            }
        }

		return mistakes;
	}
}
