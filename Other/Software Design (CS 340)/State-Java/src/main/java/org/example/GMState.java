package org.example;

public interface GMState {
    void addGumballs(GumballMachine gm, int count);
    void insertQuarter(GumballMachine gm);
    void removeQuarter(GumballMachine gm);
    void turnHandle(GumballMachine gm);
}
