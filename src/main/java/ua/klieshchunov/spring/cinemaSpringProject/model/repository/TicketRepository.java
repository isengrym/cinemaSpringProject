package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    List<Ticket> findAllTicketsBySeance(@Param("Seance") Seance seance);
    boolean existsTicketBySeanceAndPlaceAndRow(Seance seance, int place, int row);
    @Override
    <S extends Ticket> S save(S entity);

}
