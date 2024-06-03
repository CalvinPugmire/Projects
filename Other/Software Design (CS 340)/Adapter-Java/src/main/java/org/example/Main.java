package org.example;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class Main {
    public static void main(String[] args) {
        ContactManager contactManager = new ContactManager();
        contactManager.addContact(new Contact("Bob", "Smith", "123", "a@gmail.com"));
        contactManager.addContact(new Contact("John", "Joe", "456", "b@gmail.com"));
        contactManager.addContact(new Contact("Larry", "Rip", "789", "c@gmail.com"));

        Writer output = new OutputStreamWriter(System.out);
        Adapter adapterManager = new Adapter(contactManager);

        Table table = new Table(output, adapterManager);

        try {
            table.display();
            output.flush();
        } catch (IOException e) {

        }

        System.out.println();

        StringDecCaps stringDecCaps = new StringDecCaps(new StringHardOne());
        System.out.println(stringDecCaps.next());

        StringDecBackwards stringDecBackwards = new StringDecBackwards(new StringDecLows((new StringHardTwo())));
        System.out.println(stringDecBackwards.next());
    }
}