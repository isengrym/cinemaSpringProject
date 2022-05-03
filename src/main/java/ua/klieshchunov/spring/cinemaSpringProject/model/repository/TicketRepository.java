package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;

import java.util.List;

public interface TicketRepository extends PagingAndSortingRepository<Ticket, Integer> {
    List<Ticket> findAllTicketsBySeance(@Param("Seance") Seance seance);
    boolean existsTicketBySeanceAndPlaceAndRow(Seance seance, int place, int row);

    @Query("select ticket from Ticket ticket where ticket.user = :user order by ticket.seance.startDateEpochSeconds desc")
    Page<Ticket> findAllByUser(User user, Pageable pageable);
    @Override
    <S extends Ticket> S save(S entity);

}
