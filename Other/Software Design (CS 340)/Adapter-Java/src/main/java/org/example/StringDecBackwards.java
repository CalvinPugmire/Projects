package org.example;

public class StringDecBackwards implements StringSource {
    StringSource stringSource;

    public StringDecBackwards(StringSource stringSource) {
        this.stringSource = stringSource;
    }

    @Override
    public String next() {
        String result = stringSource.next();

        String tluser = "";

        char ch;
        for (int i=0; i<result.length(); i++)
        {
            ch= result.charAt(i); //extracts each character
            tluser= ch+tluser; //adds each character in front of the existing string
        }

        return tluser;
    }
}
