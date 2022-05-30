package ua.klieshchunov.spring.cinemaSpringProject.service.impl.date;

import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.service.DateService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class DateServiceImpl implements DateService {
    @Override
    public int convertStringDateToEpoch(String dateString) {
        LocalDateTime parsedDate = LocalDateTime.parse(dateString);
        return (int)parsedDate.toEpochSecond(ZoneOffset.UTC);
    }

    @Override
    public boolean isInThePast(LocalDateTime date) {
        return busyBeginsBeforeVerifiable(date, LocalDateTime.now());
    }

    @Override
    public boolean isBefore9amOrAfter10pm(LocalDateTime date) {
        if (date.getHour() < 9)
            return true;
        if (date.getHour() == 22 && date.getMinute() != 0)
            return true;
        if (date.getHour() > 22)
            return true;

        return false;
    }

    @Override
    public boolean isCrossingIntervals(Interval verifiableInterval, List<Interval> busyIntervals) {
        for (Interval busyInterval : busyIntervals) {
            if (busyEndsAfterVerifiableBeginning(verifiableInterval, busyInterval))
                if (busyBeginsBeforeVerifiable(busyInterval.beginning, verifiableInterval.beginning) ||
                        busyBeginsBetweenVerifiableStartAndEnd(busyInterval.beginning, verifiableInterval.end))
                    return true;
        }
        return false;
    }

    private boolean busyEndsAfterVerifiableBeginning(Interval verifiableInterval, Interval busyInterval) {
        return busyInterval.end.isAfter(verifiableInterval.beginning);
    }

    private boolean busyBeginsBetweenVerifiableStartAndEnd(LocalDateTime busyInterval, LocalDateTime verifiableInterval) {
        return busyInterval.isBefore(verifiableInterval);
    }

    private boolean busyBeginsBeforeVerifiable(LocalDateTime busyInterval, LocalDateTime intervalToCheck) {
        return busyInterval.isBefore(intervalToCheck);
    }
}
