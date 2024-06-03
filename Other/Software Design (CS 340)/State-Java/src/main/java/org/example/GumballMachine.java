package org.example;

public class GumballMachine {
    int coins = 0;
    int gumballs = 0;
    GMState gmState = new NoGumballsNoQuarterGM();

    void addGumballs(int count) {
        gmState.addGumballs(this, count);
    }
    void insertQuarter() {
        gmState.insertQuarter(this);
    }
    void removeQuarter() {
        gmState.removeQuarter(this);
    }
    void turnHandle() {
        gmState.turnHandle(this);
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getGumballs() {
        return gumballs;
    }

    public void setGumballs(int gumballs) {
        this.gumballs = gumballs;
    }

    public GMState getGmState() {
        return gmState;
    }

    public void setGmState(GMState gmState) {
        this.gmState = gmState;
    }
}
