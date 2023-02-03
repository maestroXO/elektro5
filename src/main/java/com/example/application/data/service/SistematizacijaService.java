package com.example.application.data.service;

import com.example.application.data.entity.Sistematizacija;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SistematizacijaService {

    private final SistematizacijaRepository repository;

    public SistematizacijaService(SistematizacijaRepository repository) {
        this.repository = repository;
    }

    public Optional<Sistematizacija> get(Long id) {
        return repository.findById(id);
    }

    public Sistematizacija update(Sistematizacija entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Sistematizacija> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Sistematizacija> list(Pageable pageable, Specification<Sistematizacija> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
