package org.example;

public class YesGumballsNoQuarterGM implements GMState {
    @Override
    public void addGumballs(GumballMachine gm, int count) {
        gm.setGumballs(gm.getGumballs()+count);
    }

    @Override
    public void insertQuarter(GumballMachine gm) {
        gm.setGmState(new YesGumballsYesQuarterGM());
    }

    @Override
    public void removeQuarter(GumballMachine gm) {
        System.out.println("No quarter to remove!");
    }

    @Override
    public void turnHandle(GumballMachine gm) {
        System.out.println("No quarter!");
    }
}
