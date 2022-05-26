package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;

import java.util.List;

public interface TicketRepository extends PagingAndSortingRepository<Ticket, Integer> {
    List<Ticket> findAllTicketsByShowtime(@Param("Seance") Showtime showtime);
    boolean existsTicketByShowtimeAndPlaceAndRow(Showtime showtime, int place, int row);

    @Query("select ticket from Ticket ticket where ticket.user = :user order by ticket.showtime.startDateEpochSeconds desc")
    Page<Ticket> findAllByUser(User user, Pageable pageable);

    @NonNull
    @Override
    <S extends Ticket> S save(@NonNull S entity);

}
