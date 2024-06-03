package spellcheck;

import java.io.IOException;
import java.net.URL;

public interface Fetcher {
    public String fetch(URL url) throws IOException;
}
