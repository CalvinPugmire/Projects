
package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;
import java.net.URL;
import java.util.SortedMap;


public class Main {
	public static void main(String[] args) {
		try {
			URL url = new URL(args[0]);
			Injector injector = Guice.createInjector(new SimpleModule());
			SpellingChecker checker1 = injector.getInstance(SpellingChecker.class);
			SortedMap<String, Integer> mistakes = checker1.check(url);
			System.out.println(mistakes);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

