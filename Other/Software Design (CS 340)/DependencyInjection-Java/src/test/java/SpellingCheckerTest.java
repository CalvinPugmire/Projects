import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.SimpleModule;
import org.example.SpellingChecker;

import java.io.IOException;
import java.net.URL;
import java.util.SortedMap;

public class SpellingCheckerTest {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://abc");
            Injector injector = Guice.createInjector(new SimpleModuleTest());
            SpellingChecker checker1 = injector.getInstance(SpellingChecker.class);
            SortedMap<String, Integer> mistakes = checker1.check(url);
            System.out.println(mistakes);

            assert(mistakes.size() == 3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
