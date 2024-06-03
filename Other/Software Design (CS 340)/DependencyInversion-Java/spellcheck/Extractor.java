package spellcheck;

import java.util.List;

public interface Extractor {
    public List<String> extract(String content);
}
