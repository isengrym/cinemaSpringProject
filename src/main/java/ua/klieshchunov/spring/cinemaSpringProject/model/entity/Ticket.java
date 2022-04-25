package ua.klieshchunov.spring.cinemaSpringProject.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name="tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ticket_id")
    private int ticketId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="seance_id")
    private Seance seance;

    @Column(name="hall_row")
    private int row;

    @Column(name="hall_place")
    private int place;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return ticketId == ticket.ticketId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }
}
