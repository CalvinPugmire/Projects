public class Algorithms {
    public static <T extends Comparable<T>> Stats<T> calcStats(T[] statsList) {
        Stats<T> statsObj = new Stats<>();

        for (T statItem : statsList) {
            if (statsObj.min == null || statItem.compareTo(statsObj.min) < 0) {
                statsObj.min = statItem;
            }
            if (statsObj.max == null || statItem.compareTo(statsObj.max) > 0) {
                statsObj.max = statItem;
            }
        }

        return statsObj;
    }
}
