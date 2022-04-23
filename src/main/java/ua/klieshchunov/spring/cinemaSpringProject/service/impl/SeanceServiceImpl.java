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

    /**
     * Method is beign used to get list of dates, which is than used to form
     * map Map<LocalDate,List<Seance>>
     * @param seances list of seances
     * @return list of dates where there are seances
     */
    @Override
    public List<LocalDate> collectDatesOfSeances(List<Seance> seances) {
        List<LocalDate> dates = new LinkedList<>();
        for (Seance seance : filterPastSeances(seances)) {
            LocalDate date = seance.getStartDateTime().toLocalDate();
            if (!dates.contains(date)) dates.add(date);
        }
        return dates;
    }

    /**
     * Method is being used to group seances by dates (e.g. days)
     * @param dates list of dates, for which there are seances
     * @param seances full list of seances
     * @return map where date corresponds to the list of seances
     */
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


    /**
     * Method is being used all seances (filterPastSeances() method is being called
     * to return only future ones)
     * @return list of Seance objects
     */
    @Override
    public List<Seance> findAll() {
        List<Seance> seances = seanceRepository.findAll();
        return filterPastSeances(seances);
    }

    /**
     * Method is being used to get seances for movie.
     * @param movie movie for which seances are going to be searched
     * @return list of seances for particular Movie object, which is given
     * as a parameter
     */
    @Override
    public List<Seance> findAllByMovie(Movie movie) {
        List<Seance> seances = seanceRepository.findAllByMovie(movie);
        return filterPastSeances(seances);
    }

    /**
     * Method is the version of findAll() method with pagination and sorting
     * @param pageNumber number of page
     * @param pageSize number of elements on each page
     * @param sortBy by which field will be sorted by
     * @param sortDirection direction of the sorting (descending, ascending)
     * @return Page object which contains limited list of seances
     */

    @Override
    public Page<Seance> findAllPaginatedSorted(Integer pageNumber, Integer pageSize,
                                               String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        Page<Seance> pagedResult = seanceRepository.findAll(paging);

        return pagedResult;
    }

    /**
     * Method is being used to get Seance object by id.
     * @param id id of seance
     * @return Seance-object filled from DB
     */
    @Override
    public Seance findById(int id) {
        return seanceRepository.findById(id);
    }

    /**
     * Method is being used to filter list of seances and return only seances, that
     * are going to be in the future
     * @param seances non filtered seances
     * @return a list without seances, which start date has already passed
     */
    private List<Seance> filterPastSeances(List<Seance> seances) {
        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        return seances
                .stream()
                .filter(seance -> seance.getStartDateTime().toEpochSecond(ZoneOffset.UTC) > currentTime)
                .collect(Collectors.toList());
    }



}
