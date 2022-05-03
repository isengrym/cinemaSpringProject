package ua.klieshchunov.spring.cinemaSpringProject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.SeanceRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.SeanceServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeanceServiceTest {
    Seance expected;
    @Mock
    SeanceRepository seanceRepository;
    @InjectMocks
    SeanceServiceImpl seanceService;

    @BeforeEach
    void setSeance() {
        expected = new Seance();
        expected.setId(1);
        expected.setMovie(new Movie());
        expected.setTicketPrice(100);
        expected.setFreePlaces(77);
        expected.setStartDateEpochSeconds(12000);
    }

    @Test
    void shouldReturnSeancesSplitByDatesInMap() {
        List<Seance> seances = prepareListOfSeances();

        Map<LocalDate, List<Seance>> expected = createExpectedMap(seances);
        Map<LocalDate, List<Seance>> actual = seanceService.collectSeancesByDate(seances);

        assertEquals(expected, actual);
    }

    private List<Seance> prepareListOfSeances() {
        List<Seance> seances = new ArrayList<>();
        Seance seanceOne = new Seance(
                1, new Movie(), 1652943600, 100, 77); //19.05.2022 7:00
        Seance seanceTwo = new Seance(
                2, new Movie(), 1652868000, 100, 77); //18.05.2022 10:00
        Seance seanceThree = new Seance(
                3, new Movie(), 1652961600, 100, 77); //19.05.2022 12:00

        seances.add(seanceOne);
        seances.add(seanceTwo);
        seances.add(seanceThree);

        return seances;
    }

    private Map<LocalDate, List<Seance>> createExpectedMap(List<Seance> seances) {
        Seance seanceOne = seances.get(0);
        Seance seanceTwo = seances.get(1);
        Seance seanceThree = seances.get(2);

        Map<LocalDate, List<Seance>> expected = new HashMap<>();
        LocalDate keyOne = LocalDateTime.ofEpochSecond(
                seanceOne.getStartDateEpochSeconds(), 0, ZoneOffset.UTC).toLocalDate();
        LocalDate keyTwo = LocalDateTime.ofEpochSecond(
                seanceTwo.getStartDateEpochSeconds(), 0, ZoneOffset.UTC).toLocalDate();

        List<Seance> seancesForKeyOne = new ArrayList<>();
        seancesForKeyOne.addAll(Arrays.asList(seanceOne, seanceThree));

        List<Seance> seancesForKeyTwo = new ArrayList<>();
        seancesForKeyTwo.add(seanceTwo);

        expected.put(keyOne, seancesForKeyOne);
        expected.put(keyTwo, seancesForKeyTwo);

        return expected;
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    void shouldReturnFutureSeancesPaginatedAndSorted() {
        Sort sort = Sort.by("freePlaces").ascending();
        Pageable pageable = PageRequest.of(0,2, sort);
        List<Seance> seances = new ArrayList<>();

        when(seanceRepository
                .findAll(
                        anyInt(),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(seances));

        Page<Seance> expectedPage = new PageImpl<>(seances);
        Page<Seance> actualPage = seanceService.findAllFutureSeancesPaginatedAndSorted(pageable);

        verify(seanceRepository, times(1))
                .findAll(
                        anyInt(),
                        any(Pageable.class));
        Assertions.assertEquals(expectedPage, actualPage);
    }
    @Test
    @ExtendWith(MockitoExtension.class)
    void shouldReturnSeanceFoundById() {
        int id = 1;

        when(seanceRepository.findById(anyInt())).thenReturn(expected);
        Seance actual = seanceService.findSeanceById(id);
        verify(seanceRepository, times(1)).findById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfSeanceWithNoFreePlacesGiven() {
        expected.setFreePlaces(0);

        assertThrows(NoFreePlacesException.class, () -> {
            seanceService.decrementFreePlacesQuantity(expected);
        });
    }

    @Test
    void shouldReturnTrueIfThereAreFreePlaces() {
        expected.setFreePlaces(5);
        assertTrue(seanceService.hasFreePlaces(expected));
    }





}
