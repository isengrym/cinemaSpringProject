package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Genre;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.GenreRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.GenreService;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }
}
