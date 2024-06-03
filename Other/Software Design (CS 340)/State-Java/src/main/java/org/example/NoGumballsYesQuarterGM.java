package org.example;

public class NoGumballsYesQuarterGM implements GMState {
    @Override
    public void addGumballs(GumballMachine gm, int count) {
        gm.setGumballs(gm.getGumballs()+count);
        gm.setGmState(new YesGumballsYesQuarterGM());
    }

    @Override
    public void insertQuarter(GumballMachine gm) {
        System.out.println("Quarter already in slot!");
    }

    @Override
    public void removeQuarter(GumballMachine gm) {
        gm.setGmState(new NoGumballsNoQuarterGM());
    }

    @Override
    public void turnHandle(GumballMachine gm) {
        System.out.println("No gumballs!");
    }
}
