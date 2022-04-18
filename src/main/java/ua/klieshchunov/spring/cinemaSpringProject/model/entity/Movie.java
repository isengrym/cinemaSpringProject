package ua.klieshchunov.spring.cinemaSpringProject.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    private String title;

    @Column(name="director")
    @NotNull
    private String director;

    @Column(name="production_year")
    @Size(min=1800, max=2077)
    private int productionYear;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name="genre")
    private Genre genre;

    @Column(name="duration_minutes")
    @Max(900)
    private int duration;

    @Column(name="age_restriction")
    @Max(120)
    private int ageRestriction;

    @Column(name="image_path")
    @NotNull
    private String imagePath;
}