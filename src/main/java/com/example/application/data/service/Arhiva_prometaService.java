package com.example.application.data.service;

import com.example.application.data.entity.Arhiva_prometa;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class Arhiva_prometaService {

    private final Arhiva_prometaRepository repository;

    public Arhiva_prometaService(Arhiva_prometaRepository repository) {
        this.repository = repository;
    }

    public Optional<Arhiva_prometa> get(Long id) {
        return repository.findById(id);
    }

    public Arhiva_prometa update(Arhiva_prometa entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Arhiva_prometa> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Arhiva_prometa> list(Pageable pageable, Specification<Arhiva_prometa> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
