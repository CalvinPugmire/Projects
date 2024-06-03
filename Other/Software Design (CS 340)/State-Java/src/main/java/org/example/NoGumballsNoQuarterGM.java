package org.example;

public class NoGumballsNoQuarterGM implements GMState {
    @Override
    public void addGumballs(GumballMachine gm, int count) {
        gm.setGumballs(gm.getGumballs()+count);
        gm.setGmState(new YesGumballsNoQuarterGM());
    }

    @Override
    public void insertQuarter(GumballMachine gm) {
        gm.setGmState(new NoGumballsYesQuarterGM());
    }

    @Override
    public void removeQuarter(GumballMachine gm) {
        System.out.println("No quarter to remove!");
    }

    @Override
    public void turnHandle(GumballMachine gm) {
        System.out.println("No gumballs or quarter!");
    }
}
