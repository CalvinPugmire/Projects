import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<Observer> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notify(Flight _flight, Flight flight) {
        for (Observer observer: observers) {
            observer.update(_flight, flight);
        }
    }
}
