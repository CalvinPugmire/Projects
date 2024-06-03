package org.example;

import org.example.Abstract.HolidayFactory;
import org.example.Abstract.TableclothPatternProvider;
import org.example.Abstract.WallHangingProvider;
import org.example.Abstract.YardOrnamentProvider;

public class DecorationPlacer {
    //private HalloweenTableclothPatternProvider tableclothPattern = new HalloweenTableclothPatternProvider();
    //private HalloweenWallHangingProvider wallHanging = new HalloweenWallHangingProvider();
    //private HalloweenYardOrnamentProvider yardOrnament = new HalloweenYardOrnamentProvider();

    private TableclothPatternProvider tableclothPattern;
    private WallHangingProvider wallHanging;
    private YardOrnamentProvider yardOrnament;

    public DecorationPlacer(HolidayFactory factory) {
        this.tableclothPattern = factory.getTablecloth();
        this.wallHanging = factory.getHanging();
        this.yardOrnament = factory.getOrnament();
    }

    public String placeDecorations() {
        return "Everything was ready for the party. The " + yardOrnament.getOrnament()
                + " was in front of the house, the " + wallHanging.getHanging()
                + " was hanging on the wall, and the tablecloth with " + tableclothPattern.getTablecloth()
                + " was spread over the table.";
    }
}
