package com.example.application.data.service;

import com.example.application.data.entity.Korisnik_aplikacije;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class Korisnik_aplikacijeService {

    private final Korisnik_aplikacijeRepository repository;

    public Korisnik_aplikacijeService(Korisnik_aplikacijeRepository repository) {
        this.repository = repository;
    }

    public Optional<Korisnik_aplikacije> get(Long id) {
        return repository.findById(id);
    }

    public Korisnik_aplikacije update(Korisnik_aplikacije entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Korisnik_aplikacije> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Korisnik_aplikacije> list(Pageable pageable, Specification<Korisnik_aplikacije> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
