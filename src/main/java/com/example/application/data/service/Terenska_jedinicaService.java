package com.example.application.data.service;

import com.example.application.data.entity.Terenska_jedinica;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class Terenska_jedinicaService {

    private final Terenska_jedinicaRepository repository;

    public Terenska_jedinicaService(Terenska_jedinicaRepository repository) {
        this.repository = repository;
    }

    public Optional<Terenska_jedinica> get(Long id) {
        return repository.findById(id);
    }

    public Terenska_jedinica update(Terenska_jedinica entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Terenska_jedinica> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Terenska_jedinica> list(Pageable pageable, Specification<Terenska_jedinica> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
