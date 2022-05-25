package ua.klieshchunov.spring.cinemaSpringProject.service.impl.date;

import java.time.LocalDateTime;

public class Interval {
    public LocalDateTime beginning;
    public LocalDateTime end;

    public Interval(LocalDateTime beginning, LocalDateTime end) {
        this.beginning = beginning;
        this.end = end;
    }


}
