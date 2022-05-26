package ua.klieshchunov.spring.cinemaSpringProject.service.impl.date;

import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Objects;

@EqualsAndHashCode
public class Interval {
    public LocalDateTime beginning;
    public LocalDateTime end;

    public Interval(LocalDateTime beginning, LocalDateTime end) {
        this.beginning = beginning;
        this.end = end;
    }

}
