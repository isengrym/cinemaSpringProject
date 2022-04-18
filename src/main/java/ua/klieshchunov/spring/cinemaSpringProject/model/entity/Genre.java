package ua.klieshchunov.spring.cinemaSpringProject.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="genre_id")
    private int id;

    @Column(name="genre_name")
    private String name;
}
