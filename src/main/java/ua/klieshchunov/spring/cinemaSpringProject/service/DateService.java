package ua.klieshchunov.spring.cinemaSpringProject.service;

import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.Interval;

import java.time.LocalDateTime;
import java.util.List;

public interface DateService {
    int convertStringDateToEpoch(String dateString);
    boolean isInThePast(LocalDateTime date);
    boolean isBefore9amOrAfter10pm(LocalDateTime date);
    boolean isCrossingIntervals(Interval interval, List<Interval> busyIntervals);

}
