package org.example;

import java.io.*;

public class Part2ArrayClass implements Part2Array2D {
    int[][] array2d;

    public Part2ArrayClass(int rownum, int colnum) {
        array2d = new int[rownum][colnum];
    }

    public Part2ArrayClass(String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream iis = new ObjectInputStream(fis);
            array2d = (int[][]) iis.readObject();
        } catch (Exception e) {}
    }

    public void save (String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array2d);
        } catch (Exception e) {}
    }

    public void load (String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream iis = new ObjectInputStream(fis);
            array2d = (int[][]) iis.readObject();
        } catch (Exception e) {}
    }

    @Override
    public void set(int row, int col, int value) {
        array2d[row][col] = value;
    }

    @Override
    public int get(int row, int col) {
        return array2d[row][col];
    }
}
