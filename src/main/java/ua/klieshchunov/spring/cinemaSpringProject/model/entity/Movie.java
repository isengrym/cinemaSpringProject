package ua.klieshchunov.spring.cinemaSpringProject.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@Table(name="movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="movie_id")
    private int id;

    @Column(name="title")
    @NotEmpty(message = "Must not be empty")
    private String title;

    @Column(name="director")
    @NotEmpty(message = "Must not be empty")
    private String director;

    @Column(name="production_year")
    @Min(value = 1800, message = "Must be bigger than 1800")
    @Max(value = 2077, message = "Must be less than 2077")
    private int productionYear;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name="genre")
    private Genre genre;

    @Column(name="duration_minutes")
    @Min(value = 1 , message = "Must be bigger than 1")
    @Max(value = 900 , message = "Must be less than 900")
    private int duration;

    @Column(name="age_restriction")
    @Min(value = 1 , message = "Must be bigger than 1")
    @Max(value = 120 , message = "Must be less than 120")
    private int ageRestriction;

    @Column(name="image_path")
    @NotEmpty(message = "Must not be empty")
    private String imagePath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}