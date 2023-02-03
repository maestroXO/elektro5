package com.example.application.data.service;

import com.example.application.data.entity.Radnici;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RadniciService {

    private final RadniciRepository repository;

    public RadniciService(RadniciRepository repository) {
        this.repository = repository;
    }

    public Optional<Radnici> get(Long id) {
        return repository.findById(id);
    }

    public Radnici update(Radnici entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Radnici> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Radnici> list(Pageable pageable, Specification<Radnici> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
