package org.example.Abstract;

public interface HolidayFactory {
    public TableclothPatternProvider getTablecloth();
    public WallHangingProvider getHanging();
    public YardOrnamentProvider getOrnament();
}
