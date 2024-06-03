package org.example.Halloween;

import org.example.Abstract.HolidayFactory;
import org.example.Abstract.TableclothPatternProvider;
import org.example.Abstract.WallHangingProvider;
import org.example.Abstract.YardOrnamentProvider;

public class HalloweenFactory implements HolidayFactory {
    @Override
    public TableclothPatternProvider getTablecloth() {
        return new HalloweenTableclothPatternProvider();
    }

    @Override
    public WallHangingProvider getHanging() {
        return new HalloweenWallHangingProvider();
    }

    @Override
    public YardOrnamentProvider getOrnament() {
        return new HalloweenYardOrnamentProvider();
    }
}
