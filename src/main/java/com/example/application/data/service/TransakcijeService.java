package com.example.application.data.service;

import com.example.application.data.entity.Transakcije;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TransakcijeService {

    private final TransakcijeRepository repository;

    public TransakcijeService(TransakcijeRepository repository) {
        this.repository = repository;
    }

    public Optional<Transakcije> get(Long id) {
        return repository.findById(id);
    }

    public Transakcije update(Transakcije entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Transakcije> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Transakcije> list(Pageable pageable, Specification<Transakcije> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
