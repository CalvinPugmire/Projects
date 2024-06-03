import static java.sql.DriverManager.println;

public class DeltaObserver implements Observer {
    @Override
    public void update(Flight _flight, Flight flight) {
        System.out.println(flight.delta(_flight));
    }
}
