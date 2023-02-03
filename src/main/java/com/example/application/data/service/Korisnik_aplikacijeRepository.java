package com.example.application.data.service;

import com.example.application.data.entity.Korisnik_aplikacije;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Korisnik_aplikacijeRepository
        extends
            JpaRepository<Korisnik_aplikacije, Long>,
            JpaSpecificationExecutor<Korisnik_aplikacije> {

}
