package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.SeanceRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.CurrentTime;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.Interval;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeanceServiceImpl implements SeanceService {

    private final SeanceRepository seanceRepository;

    @Autowired
    public SeanceServiceImpl(SeanceRepository seanceRepository) {
        this.seanceRepository = seanceRepository;
    }

    @Override
    public Map<LocalDate,List<Seance>> collectSeancesByDate(List<Seance> seances) {
        List<LocalDate> dates = collectDatesOfSeances(seances);
        Map<LocalDate,List<Seance>> seancesByDaysMap = new TreeMap<>();

        for(LocalDate date : dates) {
            List<Seance> seancesOnGivenDay = groupSeancesByGivenDay(seances, date);
            seancesByDaysMap.put(date,seancesOnGivenDay);
        }

        return seancesByDaysMap;
    }

    private List<LocalDate> collectDatesOfSeances(List<Seance> seances) {
        List<LocalDate> dates = new LinkedList<>();
        List<Seance> onlyFutureSeances = seances;

        for (Seance seance : onlyFutureSeances) {
            LocalDate date = seance.getStartDateTime().toLocalDate();
            if (!dates.contains(date))
                dates.add(date);
        }

        return dates;
    }

    @Override
    public List<Interval> collectIntervalsOfSeances(List<Seance> seances, LocalDate day) {
        List<Seance> seancesForGivenDay = groupSeancesByGivenDay(seances, day);
        List<Interval> busyIntervalsForGivenDay = new ArrayList<>();
        Interval interval;

        for (Seance seance : seancesForGivenDay) {
            LocalDateTime start = seance.getStartDateTime();
            LocalDateTime end = seance.getEndDateTime();

            interval = new Interval(start, end);
            busyIntervalsForGivenDay.add(interval);
        }

        return busyIntervalsForGivenDay;
    }

    private List<Seance> groupSeancesByGivenDay(List<Seance> seances, LocalDate day) {
        return seances.stream()
                .filter(seance -> seance.getStartDateTime().getYear() == day.getYear())
                .filter(seance -> seance.getStartDateTime().getMonth() == day.getMonth())
                .filter(seance -> seance.getStartDateTime().getDayOfMonth() == day.getDayOfMonth())
                .collect(Collectors.toList());
    }

    @Override
    public List<Seance> findAllFutureSeances() {
        return seanceRepository.findAll();
    }

    @Override
    public List<Seance> findAllFutureSeancesForMovie(Movie movie) {
        int currentTime = CurrentTime.get();
        return seanceRepository.findAllByMovie(currentTime, movie);
    }

    @Override
    public Page<Seance> findAllFutureSeancesPaginatedAndSorted(Pageable pageable) {
        int currentTime = CurrentTime.get();
        return seanceRepository
                .findAll(currentTime, pageable);
    }

    @Override
    public Page<Seance> findAllFutureSeancesForMoviePaginatedAndSorted(Pageable pageable, Movie movie) {
        int currentTime = CurrentTime.get();
        return seanceRepository
                .findAllByMovie(currentTime, movie, pageable);
    }

    @Override
    public Seance findSeanceById(int id) {
        return seanceRepository.findById(id);
    }

    @Override
    public void decrementFreePlacesQuantity(Seance seance) throws NoFreePlacesException {
        if (hasFreePlaces(seance))
            seanceRepository.decrementFreePlaces(seance.getId());
        else
            throw new NoFreePlacesException("No free places available by the seance");
    }

    @Override
    public boolean hasFreePlaces(Seance seance) {
        int freePlaces = seance.getFreePlaces();
        return freePlaces > 0;
    }

    @Override
    public boolean hasAlreadyEnded(Seance seance) {
        int currentTime = CurrentTime.get();
        return seance.getStartDateEpochSeconds() < currentTime;
    }

    @Override
    public void addSeance(Seance seance) {
        seanceRepository.save(seance);
    }
}
