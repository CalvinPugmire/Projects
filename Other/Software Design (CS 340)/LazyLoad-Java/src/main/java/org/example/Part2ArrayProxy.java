package org.example;

public class Part2ArrayProxy implements Part2Array2D {
    String filename;
    Part2ArrayClass array2d;

    public Part2ArrayProxy(String filename) {
        this.filename = filename;
    }

    public void firstLoad() {
        if (array2d == null) {
            array2d = new Part2ArrayClass(filename);
        }
    }

    public void save (String filename) {
        firstLoad();
        array2d.save(filename);
    }

    public void load (String filename) {
        firstLoad();
        array2d.load(filename);
    }

    @Override
    public void set(int row, int col, int value) {
        firstLoad();
        array2d.set(row, col, value);
    }

    @Override
    public int get(int row, int col) {
        firstLoad();
        return array2d.get(row, col);
    }

    public Part2ArrayClass getArray2d() {
        return array2d;
    }
}
