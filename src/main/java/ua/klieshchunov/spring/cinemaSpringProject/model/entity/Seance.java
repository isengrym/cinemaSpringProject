package ua.klieshchunov.spring.cinemaSpringProject.model.entity;

import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Entity
@Table(name = "seances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="seance_id")
    private int id;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name="movie_id")

    private Movie movie;

    @Column(name="start_date_seconds")
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
        Seance seance = (Seance) o;
        return id == seance.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
