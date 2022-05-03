package ua.klieshchunov.spring.cinemaSpringProject.model.repository.Impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Movie;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.SeanceRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SeanceRepositoryImpl implements SeanceRepository {
    private final EntityManager entityManager;
    private final String GET_ALL_SEANCES_QUERY = "select seance from Seance seance where seance.startDateEpochSeconds > :currentTime";
    private final String GET_ALL_SEANCES_FOR_MOVIE_QUERY = "select seance from Seance seance where seance.startDateEpochSeconds > :currentTime and seance.movie =:movie";
    private final String UPDATE_SEANCE_DECREMENT_FREE_PLACES = "update Seance seance set seance.freePlaces = seance.freePlaces-1 where seance.id = :id";

    @Autowired
    public SeanceRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Seance findById(int id) {
        return entityManager.find(Seance.class, id);
    }

    @Override
    public List<Seance> findAll() {
        final int currentTime = (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        List<Seance> seances = entityManager
                .createQuery(GET_ALL_SEANCES_QUERY)
                .setParameter("currentTime", currentTime)
                .getResultList();

        return seances;
    }

    @Override
    public List<Seance> findAllByMovie(Movie movie) {
        final int currentTime = (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        List<Seance> seances = entityManager
                .createQuery(GET_ALL_SEANCES_FOR_MOVIE_QUERY)
                .setParameter("currentTime", currentTime)
                .setParameter("movie", movie)
                .getResultList();

        return seances;
    }

    @Override
    public Page<Seance> findAllFutureSeancesPaginatedAndSorted(int currentTime, Pageable pageable) {
        List<Seance> seances = new ArrayList<>();
        Page<Seance> page = new PageImpl<>(seances, pageable,seances.size());
        return null;

    }
    @Override
    public void decrementFreePlaces(int id) {
        entityManager
                .createQuery(UPDATE_SEANCE_DECREMENT_FREE_PLACES)
                .setParameter("id", id)
                .executeUpdate();
    }
}
