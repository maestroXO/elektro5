package com.example.application.data.service;

import com.example.application.data.entity.Oprema;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class OpremaService {

    private final OpremaRepository repository;

    public OpremaService(OpremaRepository repository) {
        this.repository = repository;
    }

    public Optional<Oprema> get(Long id) {
        return repository.findById(id);
    }

    public Oprema update(Oprema entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Oprema> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Oprema> list(Pageable pageable, Specification<Oprema> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
