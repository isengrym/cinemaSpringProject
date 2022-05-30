package ua.klieshchunov.spring.cinemaSpringProject.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeDto {
    public int id;
    public Movie movie;
    public int ticketPrice;
    public LocalDateTime startDateTimeBeforeUpdating;
    public LocalDateTime startDateTimeAfterUpdating;
    public int freePlaces;
    public boolean isForUpdating;

    public static ShowtimeDto toDto(Showtime showtime) {
        ShowtimeDto showtimeDto = new ShowtimeDto();
        showtimeDto.id = showtime.getId();
        showtimeDto.movie = showtime.getMovie();
        showtimeDto.ticketPrice = showtime.getId();
        showtimeDto.startDateTimeBeforeUpdating = showtime.getStartDateTime();
        showtimeDto.startDateTimeAfterUpdating = showtimeDto.startDateTimeBeforeUpdating;
        showtimeDto.freePlaces = showtime.getFreePlaces();
        showtimeDto.isForUpdating = false;

        return showtimeDto;
    }
    public static ShowtimeDto toDto(Showtime showtime, LocalDateTime newStartDateTime) {
        ShowtimeDto showtimeDto = toDto(showtime);
        showtimeDto.startDateTimeAfterUpdating = newStartDateTime;
        showtimeDto.isForUpdating = true;
        return showtimeDto;
    }

    public static Showtime toEntityWithOldStartTime(ShowtimeDto showtimeDto) {
        Showtime showtime = new Showtime();
        showtime.setId(showtimeDto.id);
        showtime.setMovie(showtimeDto.movie);
        showtime.setStartDateEpochSeconds((int)showtimeDto.startDateTimeBeforeUpdating.toEpochSecond(ZoneOffset.UTC));
        showtime.setTicketPrice(showtimeDto.ticketPrice);
        showtime.setFreePlaces(showtimeDto.freePlaces);

        return showtime;
    }

    public static Showtime toEntityWithNewStartTime(ShowtimeDto showtimeDto) {
        Showtime showtime = ShowtimeDto.toEntityWithOldStartTime(showtimeDto);
        showtime.setStartDateEpochSeconds((int)showtimeDto.startDateTimeAfterUpdating.toEpochSecond(ZoneOffset.UTC));

        return showtime;
    }
}
