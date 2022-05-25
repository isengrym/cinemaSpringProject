package ua.klieshchunov.spring.cinemaSpringProject.service.impl.date;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CurrentTime {
    public static int get() {
        return (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
