package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.repository.SeanceRepository;
import ua.klieshchunov.spring.cinemaSpringProject.service.SeanceService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class SeanceServiceImpl implements SeanceService {

    private final SeanceRepository seanceRepository;

    @Autowired
    public SeanceServiceImpl(SeanceRepository seanceRepository) {
        this.seanceRepository = seanceRepository;
    }

    @Override
    public List<Seance> findAll() {
        return seanceRepository.findAll();
    }

    @Override
    public Seance findById(int id) {
        return seanceRepository.findById(id);
    }



}
