package models;

public class LocationData {
    private Location[] data;

    public Location[] getData() {
        return data;
    }

    public void setData(Location[] data) {
        this.data = data;
    }

    public Location getRandom() {
        int randomNum = (int) ((Math.random() * (data.length)) + 0);
        Location location = data[randomNum];
        return location;
    }
}
