package org.example;

public class YesGumballsYesQuarterGM implements GMState {
    @Override
    public void addGumballs(GumballMachine gm, int count) {
        gm.setGumballs(gm.getGumballs()+count);
    }

    @Override
    public void insertQuarter(GumballMachine gm) {
        System.out.println("Quarter already in slot!");
    }

    @Override
    public void removeQuarter(GumballMachine gm) {
        gm.setGmState(new YesGumballsNoQuarterGM());
    }

    @Override
    public void turnHandle(GumballMachine gm) {
        gm.setGumballs(gm.getGumballs()-1);
        gm.setCoins(gm.getCoins()+1);
        if (gm.getGumballs() == 0) {
            gm.setGmState(new NoGumballsNoQuarterGM());
        } else {
            gm.setGmState(new YesGumballsNoQuarterGM());
        }
    }
}
