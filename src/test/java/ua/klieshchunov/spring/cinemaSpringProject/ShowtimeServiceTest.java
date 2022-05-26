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
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.ShowtimeRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.exceptions.NoFreePlacesException;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.ShowtimeServiceImpl;
import ua.klieshchunov.spring.cinemaSpringProject.service.impl.date.Interval;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowtimeServiceTest {
    Showtime expected;
    @Mock
    ShowtimeRepository showtimeRepository;
    @InjectMocks
    ShowtimeServiceImpl showtimeService;

    @BeforeEach
    void setShowtime() {
        expected = new Showtime();
        expected.setId(1);
        expected.setMovie(new Movie());
        expected.setTicketPrice(100);
        expected.setFreePlaces(77);
        expected.setStartDateEpochSeconds(12000);
    }

    @Test
    void shouldReturnShowtimesSplitedByDatesInMap() {
        List<Showtime> showtimes = prepareListOfShowtimes();

        Map<LocalDate, List<Showtime>> expected = createExpectedMap(showtimes);
        Map<LocalDate, List<Showtime>> actual = showtimeService.collectShowtimesByDate(showtimes);

        assertEquals(expected, actual);
    }


    private List<Showtime> prepareListOfShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        Showtime showtimeOne = new Showtime(
                1, new Movie(), 1652943600, 100, 77); //19.05.2022 7:00
        Showtime showtimeTwo = new Showtime(
                2, new Movie(), 1652868000, 100, 77); //18.05.2022 10:00
        Showtime showtimeThree = new Showtime(
                3, new Movie(), 1652961600, 100, 77); //19.05.2022 12:00

        showtimes.add(showtimeOne);
        showtimes.add(showtimeTwo);
        showtimes.add(showtimeThree);

        return showtimes;
    }

    private Map<LocalDate, List<Showtime>> createExpectedMap(List<Showtime> showtimes) {
        Showtime showtimeOne = showtimes.get(0);
        Showtime showtimeTwo = showtimes.get(1);
        Showtime showtimeThree = showtimes.get(2);

        Map<LocalDate, List<Showtime>> expected = new HashMap<>();
        LocalDate keyOne = LocalDateTime.ofEpochSecond(
                showtimeOne.getStartDateEpochSeconds(), 0, ZoneOffset.UTC).toLocalDate();
        LocalDate keyTwo = LocalDateTime.ofEpochSecond(
                showtimeTwo.getStartDateEpochSeconds(), 0, ZoneOffset.UTC).toLocalDate();

        List<Showtime> seancesForKeyOne = new ArrayList<>(Arrays.asList(showtimeOne, showtimeThree));

        List<Showtime> seancesForKeyTwo = new ArrayList<>();
        seancesForKeyTwo.add(showtimeTwo);

        expected.put(keyOne, seancesForKeyOne);
        expected.put(keyTwo, seancesForKeyTwo);

        return expected;
    }

    @Test
    void shouldReturnListOfIntervalsCreatedFromGivenShowtime() {
        List<Showtime> showtimes = prepareListOfShowtimes();
        LocalDate day = showtimes.get(0).getStartDateTime().toLocalDate(); //19.05.2022

        List<Interval> expected = createExpectedIntervalsList(showtimes);
        List<Interval> actual = showtimeService.collectIntervalsOfShowtimes(showtimes, day);

        Assertions.assertEquals(expected, actual);

    }

    private List<Interval> createExpectedIntervalsList(List<Showtime> showtimes) {
        Interval one = new Interval(showtimes.get(0).getStartDateTime(), showtimes.get(0).getEndDateTime());
        Interval two = new Interval(showtimes.get(2).getStartDateTime(), showtimes.get(2).getEndDateTime());

        return new ArrayList<>(Arrays.asList(one, two));
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    void shouldReturnFutureShowtimesPaginatedAndSorted() {
        Sort sort = Sort.by("freePlaces").ascending();
        Pageable pageable = PageRequest.of(0,2, sort);
        List<Showtime> showtimes = new ArrayList<>();

        when(showtimeRepository
                .findAll(
                        anyInt(),
                        any(Pageable.class)))
                .thenReturn(new PageImpl<>(showtimes));

        Page<Showtime> expectedPage = new PageImpl<>(showtimes);
        Page<Showtime> actualPage = showtimeService.findAllFutureShowtimesPaginatedAndSorted(pageable);

        verify(showtimeRepository, times(1))
                .findAll(
                        anyInt(),
                        any(Pageable.class));
        Assertions.assertEquals(expectedPage, actualPage);
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    void shouldReturnShowtimeFoundById() {
        int id = 1;

        when(showtimeRepository.findById(anyInt())).thenReturn(expected);
        Showtime actual = showtimeService.findShowtimeById(id);
        verify(showtimeRepository, times(1)).findById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfShowtimeWithNoFreePlacesGiven() {
        expected.setFreePlaces(0);

        assertThrows(NoFreePlacesException.class, () ->
                showtimeService.decrementFreePlacesQuantity(expected));
    }

    @Test
    void shouldReturnTrueIfThereAreFreePlaces() {
        expected.setFreePlaces(5);
        assertTrue(showtimeService.hasFreePlaces(expected));
    }

}
