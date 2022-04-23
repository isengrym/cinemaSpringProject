package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
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
    public List<LocalDate> collectDatesOfSeances(List<Seance> seances) {
        List<LocalDate> dates = new LinkedList<>();
        for (Seance seance : seances) {
            LocalDate date = seance.getStartDateTime().toLocalDate();
            if (!dates.contains(date)) dates.add(date);
        }
        return dates;
    }

    @Override
    public Map<LocalDate,List<Seance>> collectSeancesByDate(List<LocalDate> dates, List<Seance> seances) {
        Map<LocalDate,List<Seance>> map = new LinkedHashMap<>();

        for(LocalDate date : dates) {
            List<Seance> seancesForDate = seances.stream()
                    .filter(seance -> seance.getStartDateTime().getYear() == date.getYear())
                    .filter(seance -> seance.getStartDateTime().getMonth() == date.getMonth())
                    .filter(seance -> seance.getStartDateTime().getDayOfMonth() == date.getDayOfMonth())
                    .collect(Collectors.toList());
            map.put(date,seancesForDate);
        }
        return map;
    }



    @Override
    public List<Seance> findAll() {
        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return seanceRepository.findAll();
    }

    @Override
    public List<Seance> findAllByMovie(Movie movie) {
        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return seanceRepository.findAllByMovie(movie);
    }

    @Override
    public Page<Seance> findAllPaginatedSorted(Integer pageNumber, Integer pageSize,
                                               String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        Page<Seance> pagedResult = seanceRepository.findAll(paging);

        return pagedResult;
    }

    @Override
    public Seance findById(int id) {
        return seanceRepository.findById(id);
    }



}
