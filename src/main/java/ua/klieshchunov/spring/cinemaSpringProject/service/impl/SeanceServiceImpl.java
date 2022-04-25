package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.SeanceRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;

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
            List<Seance> seancesOnGivenDay = groupSeancesByGivenDay(seances, date);
            seancesByDaysMap.put(date,seancesOnGivenDay);
        }

        return seancesByDaysMap;
    }

    private List<LocalDate> collectDatesOfSeances(List<Seance> seances) {
        List<LocalDate> dates = new LinkedList<>();
        List<Seance> onlyFutureSeances = filterPastSeances(seances);
        for (Seance seance : onlyFutureSeances) {
            LocalDate date = seance.getStartDateTime().toLocalDate();
            if (!dates.contains(date))
                dates.add(date);
        }
        return dates;
    }

    private List<Seance> groupSeancesByGivenDay(List<Seance> seances, LocalDate date) {
        return seances.stream()
                .filter(seance -> seance.getStartDateTime().getYear() == date.getYear())
                .filter(seance -> seance.getStartDateTime().getMonth() == date.getMonth())
                .filter(seance -> seance.getStartDateTime().getDayOfMonth() == date.getDayOfMonth())
                .collect(Collectors.toList());
    }

    @Override
    public List<Seance> findAllSeances() {
        List<Seance> seances = seanceRepository.findAll();
        return filterPastSeances(seances);
    }

    @Override
    public List<Seance> findAllSeancesForMovie(Movie movie) {
        List<Seance> seances = seanceRepository.findAllByMovie(movie);
        return filterPastSeances(seances);
    }


    @Override
    public Page<Seance> findAllSeancesPaginatedAndSorted(Integer pageNumber, Integer pageSize,
                                                         String sortBy, String sortOrder) {
        Pageable customizedPageable = formPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Seance> pageWithSeances = seanceRepository.findAll(customizedPageable);

        return pageWithSeances;
    }

    private Pageable formPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    @Override
    public Seance findSeanceById(int id) {
        return seanceRepository.findById(id);
    }


    private List<Seance> filterPastSeances(List<Seance> seances) {
        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        return seances
                .stream()
                .filter(seance -> seance.getStartDateTime().toEpochSecond(ZoneOffset.UTC) > currentTime)
                .collect(Collectors.toList());
    }



}
