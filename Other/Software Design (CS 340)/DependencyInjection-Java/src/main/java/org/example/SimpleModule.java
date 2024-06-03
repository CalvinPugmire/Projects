package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class SimpleModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Dictionary.class).to(TxtDictionary.class);
        bind(Extractor.class).to(WordExtractor.class);
        bind(Fetcher.class).to(URLFetcher.class);

        // If you want a singleton instance of a class to be bound everywhere it is used
//        bind(IDictionary.class).to(TextDictionary.class).asEagerSingleton();


        // If you want to bind a specific instance of a class
//        bind(SomeInterface.class).toInstance(new SomeClass());

        // or
//        SomeInterface myInstance = new SomeClass();
//        myInstance.callMethodsToConfigureTheClass();
//        bind(SomeInterface.class).toInstance(myInstance);

    }

    @Provides
    @Named("fileName")
    public String fileName() {
        return "dict.txt";
    }
}
