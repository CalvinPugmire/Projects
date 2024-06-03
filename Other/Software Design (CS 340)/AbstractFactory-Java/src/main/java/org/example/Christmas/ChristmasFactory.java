package org.example.Christmas;

import org.example.Abstract.HolidayFactory;
import org.example.Abstract.TableclothPatternProvider;
import org.example.Abstract.WallHangingProvider;
import org.example.Abstract.YardOrnamentProvider;

public class ChristmasFactory implements HolidayFactory {
    @Override
    public TableclothPatternProvider getTablecloth() {
        return new ChristmasTableclothPatternProvider();
    }

    @Override
    public WallHangingProvider getHanging() {
        return new ChristmasWallHangingProvider();
    }

    @Override
    public YardOrnamentProvider getOrnament() {
        return new ChristmasYardOrnamentProvider();
    }
}
