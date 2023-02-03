package com.example.application.data.service;

import com.example.application.data.entity.Fizicka_lokacija;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class Fizicka_lokacijaService {

    private final Fizicka_lokacijaRepository repository;

    public Fizicka_lokacijaService(Fizicka_lokacijaRepository repository) {
        this.repository = repository;
    }

    public Optional<Fizicka_lokacija> get(Long id) {
        return repository.findById(id);
    }

    public Fizicka_lokacija update(Fizicka_lokacija entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Fizicka_lokacija> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Fizicka_lokacija> list(Pageable pageable, Specification<Fizicka_lokacija> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
