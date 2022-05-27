package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.ShowtimeRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.ShowtimeService;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.CurrentTime;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.Interval;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    public Map<LocalDate,List<Showtime>> collectShowtimesByDate(List<Showtime> showtimes) {
        List<LocalDate> dates = collectDatesOfShowtimes(showtimes);
        Map<LocalDate,List<Showtime>> seancesByDaysMap = new TreeMap<>();

        for(LocalDate date : dates) {
            List<Showtime> seancesOnGivenDay = groupShowtimesByGivenDay(showtimes, date);
            seancesByDaysMap.put(date,seancesOnGivenDay);
        }

        return seancesByDaysMap;
    }

    private List<LocalDate> collectDatesOfShowtimes(List<Showtime> showtimes) {
        List<LocalDate> dates = new LinkedList<>();
        List<Showtime> onlyFutureShowtimes = showtimes;

        for (Showtime showtime : onlyFutureShowtimes) {
            LocalDate date = showtime.getStartDateTime().toLocalDate();
            if (!dates.contains(date))
                dates.add(date);
        }

        return dates;
    }

    @Override
    public List<Interval> collectIntervalsOfShowtimes(List<Showtime> showtimes, LocalDate day) {
        List<Showtime> seancesForGivenDay = groupShowtimesByGivenDay(showtimes, day);
        List<Interval> busyIntervalsForGivenDay = new ArrayList<>();
        Interval interval;

        for (Showtime showtime : seancesForGivenDay) {
            LocalDateTime start = showtime.getStartDateTime();
            LocalDateTime end = showtime.getEndDateTime();

            interval = new Interval(start, end);
            busyIntervalsForGivenDay.add(interval);
        }

        return busyIntervalsForGivenDay;
    }

    private List<Showtime> groupShowtimesByGivenDay(List<Showtime> showtimes, LocalDate day) {
        return showtimes.stream()
                .filter(seance -> seance.getStartDateTime().getYear() == day.getYear())
                .filter(seance -> seance.getStartDateTime().getMonth() == day.getMonth())
                .filter(seance -> seance.getStartDateTime().getDayOfMonth() == day.getDayOfMonth())
                .collect(Collectors.toList());
    }

    @Override
    public List<Showtime> findAllFutureShowtimes() {
        return showtimeRepository.findAll();
    }

    @Override
    public List<Showtime> findAllFutureShowtimesForMovie(Movie movie) {
        int currentTime = CurrentTime.get();
        return showtimeRepository.findAllByMovie(currentTime, movie);
    }

    @Override
    public Page<Showtime> findAllFutureShowtimesPaginatedAndSorted(Pageable pageable) {
        int currentTime = CurrentTime.get();
        return showtimeRepository
                .findAll(currentTime, pageable);
    }

    @Override
    public Page<Showtime> findAllFutureShowtimesForMoviePaginatedAndSorted(Pageable pageable, Movie movie) {
        int currentTime = CurrentTime.get();
        return showtimeRepository
                .findAllByMovie(currentTime, movie, pageable);
    }

    @Override
    public Showtime findShowtimeById(int id) {
        return showtimeRepository.findById(id);
    }

    @Override
    public void decrementFreePlacesQuantity(Showtime showtime) throws NoFreePlacesException {
        if (hasFreePlaces(showtime))
            showtimeRepository.decrementFreePlaces(showtime.getId());
        else
            throw new NoFreePlacesException("No free places available by the showtime");
    }

    @Override
    public boolean hasFreePlaces(Showtime showtime) {
        int freePlaces = showtime.getFreePlaces();
        return freePlaces > 0;
    }

    @Override
    public boolean hasAlreadyEnded(Showtime showtime) {
        int currentTime = CurrentTime.get();
        return showtime.getStartDateEpochSeconds() < currentTime;
    }

    @Override
    public void addShowtime(Showtime showtime) {
        showtimeRepository.save(showtime);
    }

    @Override
    public void deleteShowtime(Showtime showtime) {
        showtimeRepository.delete(showtime);
    }
}
