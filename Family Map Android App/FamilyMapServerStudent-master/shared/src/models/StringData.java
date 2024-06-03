package models;

public class StringData {
    private String[] data;

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getRandom() {
        int randomNum = (int) ((Math.random() * (data.length)) + 0);
        String string = data[randomNum];
        return string;
    }
}
