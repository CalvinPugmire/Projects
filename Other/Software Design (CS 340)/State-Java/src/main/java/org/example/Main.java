package org.example;

public class Main {
    public static void main(String[] args) {
        GumballMachine gm = new GumballMachine();

        gm.removeQuarter();
        gm.turnHandle();
        System.out.println();

        gm.addGumballs(1);
        gm.removeQuarter();
        gm.turnHandle();
        System.out.println();

        gm.insertQuarter();
        gm.insertQuarter();
        gm.turnHandle();
        System.out.println();

        gm.removeQuarter();
        gm.turnHandle();
        System.out.println();

        gm.insertQuarter();
        gm.insertQuarter();
        gm.turnHandle();
        System.out.println();

        gm.addGumballs(1);
        gm.turnHandle();
        System.out.println();
    }
}