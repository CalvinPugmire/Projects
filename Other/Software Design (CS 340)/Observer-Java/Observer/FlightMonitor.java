
public class FlightMonitor {
	
	public static void main(String[] args) {
	
		FlightFeed feed = new FlightFeed();

		Observer sobserver = new StatusObserver();
		feed.attach(sobserver);
		Observer dobserver = new DeltaObserver();
		feed.attach(dobserver);

		feed.start();
	}
	
}