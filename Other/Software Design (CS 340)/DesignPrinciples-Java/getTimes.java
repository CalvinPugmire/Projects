//1. What design principles does this code violate?
    //Avoid code duplication.
//2. Refactor the code to improve its design.


public void getTimes(Properties props) throws Exception {
    String valueString;
    int value;

    valueString = props.getProperty("interval");
    function1(valueString, "monitor interval");

    value = Integer.parseInt(valueString);
    function2(value, "monitor interval > 0");
    checkInterval = value;

    valueString = props.getProperty("duration")
    function1(valueString, "duration");

    value = Integer.parseInt(valueString);
    function2(value, "duration > 0");
    function3(value, "duration % checkInterval");
    monitorTime = value;

    function1(props.getProperty("departure"), "departure offset");

    value = Integer.parseInt(valueString);
    function2(value, "departure > 0");
    function3(value, "departure % checkInterval");
    departureOffset = value;
}

public void function1 (String valueString, String message) {
    if (valueString == null) {
        throw new MissingPropertiesException(message);
    }
}

public void function2 (int value, String message) {
    if (value <= 0) {
        throw new MissingPropertiesException(message);
    }
}

public void function3 (int value, String message) {
    if ((value % checkInterval) != 0) {
        throw new MissingPropertiesException(message);
    }
}

