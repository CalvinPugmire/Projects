import static java.sql.DriverManager.println;

public class StatusObserver implements Observer {
    @Override
    public void update(Flight _flight, Flight flight) {
        System.out.println(flight.toString());
    }
}
