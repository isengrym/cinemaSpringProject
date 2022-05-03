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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        Map<LocalDate,List<Seance>> seancesByDaysMap = new LinkedHashMap<>();

        for(LocalDate date : dates) {
            List<Seance> seancesOnGivenDay = leaveSeancesThatMatchGivenDay(seances, date);
            seancesByDaysMap.put(date,seancesOnGivenDay);
        }
        return seancesByDaysMap;
    }

    private List<LocalDate> collectDatesOfSeances(List<Seance> seances) {
        List<LocalDate> dates = new LinkedList<>();

        for (Seance seance : seances) {
            LocalDate date = seance.getStartDateTime().toLocalDate();
            if (!dates.contains(date))
                dates.add(date);
        }

        return dates;
    }

    private List<Seance> leaveSeancesThatMatchGivenDay(List<Seance> seances, LocalDate date) {
        return seances.stream()
                .filter(seance -> seance.getStartDateTime().getYear() == date.getYear())
                .filter(seance -> seance.getStartDateTime().getMonth() == date.getMonth())
                .filter(seance -> seance.getStartDateTime().getDayOfMonth() == date.getDayOfMonth())
                .collect(Collectors.toList());
    }

    @Override
    public List<Seance> findAllFutureSeances() {
        List<Seance> seances = seanceRepository.findAll();
        return seances;
    }

    @Override
    public List<Seance> findAllFutureSeancesForMovie(Movie movie) {
        List<Seance> seances = seanceRepository.findAllByMovie(movie);
        return seances;
    }

    @Override
    public Page<Seance> findAllFutureSeancesPaginatedAndSorted(Pageable pageable) {
        int currentTime = (int)LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return seanceRepository
                .findAllFutureSeancesPaginatedAndSorted(currentTime, pageable);
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
}
