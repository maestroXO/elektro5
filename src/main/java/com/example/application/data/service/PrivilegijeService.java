package com.example.application.data.service;

import com.example.application.data.entity.Privilegije;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PrivilegijeService {

    private final PrivilegijeRepository repository;

    public PrivilegijeService(PrivilegijeRepository repository) {
        this.repository = repository;
    }

    public Optional<Privilegije> get(Long id) {
        return repository.findById(id);
    }

    public Privilegije update(Privilegije entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Privilegije> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Privilegije> list(Pageable pageable, Specification<Privilegije> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
