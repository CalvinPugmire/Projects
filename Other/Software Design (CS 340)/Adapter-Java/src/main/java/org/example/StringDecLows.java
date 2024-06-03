package org.example;

public class StringDecLows implements StringSource {
    StringSource stringSource;

    public StringDecLows(StringSource stringSource) {
        this.stringSource = stringSource;
    }

    @Override
    public String next() {
        String result = stringSource.next();
        return result.toLowerCase();
    }
}
