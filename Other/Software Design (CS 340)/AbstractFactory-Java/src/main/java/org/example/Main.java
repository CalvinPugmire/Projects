package org.example;

import org.example.Christmas.ChristmasFactory;
import org.example.Halloween.HalloweenFactory;

public class Main {

    public static void main(String[] args) {
        HalloweenFactory halloweenFactory = new HalloweenFactory();

        DecorationPlacer decorationPlacer1 = new DecorationPlacer(halloweenFactory);

        System.out.println(decorationPlacer1.placeDecorations());

        ChristmasFactory christmasFactory = new ChristmasFactory();

        DecorationPlacer decorationPlacer2 = new DecorationPlacer(christmasFactory);

        System.out.println(decorationPlacer2.placeDecorations());
    }
}
