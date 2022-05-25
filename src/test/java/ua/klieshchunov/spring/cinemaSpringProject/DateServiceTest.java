package ua.klieshchunov.spring.cinemaSpringProject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.klieshchunov.spring.cinemaSpringProject.service.DateService;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.DateServiceImpl;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.Interval;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateServiceTest {
    DateService dateService = new DateServiceImpl();

    LocalDateTime beforeStartDate;
    LocalDateTime beforeEndDate;
    Interval before;

    LocalDateTime betweenStartDate;
    LocalDateTime betweenEndDate;
    Interval between;

    LocalDateTime afterStartDate;
    LocalDateTime afterEndDate;
    Interval after;

    List<Interval> busyIntervals;
    @BeforeEach
    public void createTestingData() {
        beforeStartDate = LocalDateTime.of(2022,1,5,13,0);
        beforeEndDate = LocalDateTime.of(2022,1,5,15,20);
        before = new Interval(beforeStartDate, beforeEndDate);

        betweenStartDate = LocalDateTime.of(2022,1,5,15,30);
        betweenEndDate = LocalDateTime.of(2022,1,5,16,50);
        between = new Interval(betweenStartDate, betweenEndDate);

        afterStartDate = LocalDateTime.of(2022,1,5,17,0);
        afterEndDate = LocalDateTime.of(2022,1,5,18,30);
        after = new Interval(afterStartDate, afterEndDate);

        busyIntervals = new ArrayList<>(Arrays.asList(before, after));
    }

    @Test
    public void isInThePastShouldReturnFalse() {
        between.beginning = between.beginning.plusYears(55);
        Assertions.assertFalse(dateService.isInThePast(between.beginning));
    }

    @Test
    public void isInThePastShouldReturnTrue() {
        Assertions.assertTrue(dateService.isInThePast(between.beginning));
    }

    @Test
    public void isCrossingIntervalsShouldReturnFalse() {
        Assertions.assertFalse(dateService.isCrossingIntervals(between, busyIntervals));
    }

    @Test
    public void isCrossingIntervalsShouldReturnTrue() {
        LocalDateTime betweenStartDate = LocalDateTime.of(2022,1,5,16,0);
        LocalDateTime betweenEndDate = LocalDateTime.of(2022,1,5,16,30);
        Interval crossing = new Interval(betweenStartDate, betweenEndDate);
        busyIntervals.add(crossing);

        Assertions.assertTrue(dateService.isCrossingIntervals(between, busyIntervals));
    }

    @Test
    public void isBefore9amShouldReturnFalse() {
        Assertions.assertFalse(dateService.isBefore9amOrAfter10pm(before.beginning));
        Assertions.assertFalse(dateService.isBefore9amOrAfter10pm(after.end));
    }

    @Test
    public void isBefore9amOr22pmShouldReturnTrue() {
        LocalDateTime _8hours30minutes = LocalDateTime.of(2022,1,1,8,30);
        LocalDateTime _22hours_30minutes =  LocalDateTime.of(2022,1,1,22,30);
        LocalDateTime _23hours_00minutes =  LocalDateTime.of(2022,1,1,23,0);

        Assertions.assertTrue(dateService.isBefore9amOrAfter10pm(_8hours30minutes));
        Assertions.assertTrue(dateService.isBefore9amOrAfter10pm(_22hours_30minutes));
        Assertions.assertTrue(dateService.isBefore9amOrAfter10pm(_23hours_00minutes));
    }
}
