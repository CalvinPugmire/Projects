package org.example;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

public class Part1ExerciseProxy implements Part1ExerciseInterface {
    private Hashtable<String,String> allowedDays;
    private int minTime;
    private int maxTime;
    private LocalDateTime dateTime;
    private Part1Part1ExerciseClass exercise;

    public Part1ExerciseProxy() {
        allowedDays = new Hashtable<>();
        allowedDays.put("mondayOperation", "MONDAY");
        allowedDays.put("tuesdayOperation", "TUESDAY");
        allowedDays.put("wednesdayOperation", "WEDNESDAY");
        allowedDays.put("thursdayOperation", "THURSDAY");
        allowedDays.put("fridayOperation", "FRIDAY");
        allowedDays.put("saturdayOperation", "SATURDAY");
        allowedDays.put("sundayOperation", "SUNDAY");

        minTime = 8;
        maxTime = 17;

        exercise = new Part1Part1ExerciseClass();
    }

    private void evaluation (String operation) {
        dateTime = LocalDateTime.now();
        DayOfWeek currentDay = dateTime.getDayOfWeek();
        if (!allowedDays.get(operation).contains(currentDay.toString())) {
            throw new RuntimeException("Wrong day.");
        }

        int hour = dateTime.getHour();
        if (!(hour >= minTime && hour <= maxTime)) {
            throw new RuntimeException("Wrong time.");
        }
    }

    @Override
    public void mondayOperation() {
        evaluation("mondayOperation");

        exercise.mondayOperation();
    }

    @Override
    public void tuesdayOperation() {
        evaluation("tuesdayOperation");

        exercise.tuesdayOperation();
    }

    @Override
    public void wednesdayOperation() {
        evaluation("wednesdayOperation");

        exercise.wednesdayOperation();
    }

    @Override
    public void thursdayOperation() {
        evaluation("thursdayOperation");

        exercise.thursdayOperation();
    }

    @Override
    public void fridayOperation() {
        evaluation("fridayOperation");

        exercise.fridayOperation();
    }

    @Override
    public void saturdayOperation() {
        evaluation("saturdayOperation");

        exercise.saturdayOperation();
    }

    @Override
    public void sundayOperation() {
        evaluation("sundayOperation");

        exercise.sundayOperation();
    }
}
