public class Maximizer<T extends Comparable<T>> {
    T currentMax;

    public Maximizer() {
        currentMax = null;
    }

    public T getValue() {
        return currentMax;
    }

    public void updateValue(T value) {
        if (currentMax == null || value.compareTo(currentMax) > 0) {
            currentMax = value;
        }
    }
}
