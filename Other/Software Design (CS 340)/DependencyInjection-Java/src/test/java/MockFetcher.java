import org.example.Fetcher;

import java.io.IOException;
import java.net.URL;

public class MockFetcher implements Fetcher {
    @Override
    public String fetch(URL url) throws IOException {
        return "I have te high grnd!";
    }
}
