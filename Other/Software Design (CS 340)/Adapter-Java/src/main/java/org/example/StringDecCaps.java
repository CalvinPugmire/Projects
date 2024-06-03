package org.example;

public class StringDecCaps implements StringSource {
    StringSource stringSource;

    public StringDecCaps (StringSource stringSource) {
        this.stringSource = stringSource;
    }

    @Override
    public String next() {
        String result = stringSource.next();
        return result.toUpperCase();
    }
}
