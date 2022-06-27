package ua.klieshchunov.spring.cinemaSpringProject.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Entity
@Table(name = "showtime")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="showtime_id")
    private int id;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name="movie_id")

    private Movie movie;

    @Column(name="start_time")
    @Min(1653508592) //Soundtrack 2 My Life - Kid Cudi
    private Integer startDateEpochSeconds;

    @Column(name="ticket_price")
    @Min(1)
    @Max(400)
    private Integer ticketPrice;

    @Column(name="free_places")
    private Integer freePlaces;

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.ofEpochSecond(this.startDateEpochSeconds, 0, ZoneOffset.UTC);
    }

    public LocalDateTime getEndDateTime() {
        LocalDateTime startTime = getStartDateTime();
        return startTime.plusMinutes(this.movie.getDuration());
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(getStartDateTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Showtime showtime = (Showtime) o;
        return id == showtime.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
