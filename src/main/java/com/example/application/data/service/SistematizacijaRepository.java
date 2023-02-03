package com.example.application.data.service;

import com.example.application.data.entity.Sistematizacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SistematizacijaRepository
        extends
            JpaRepository<Sistematizacija, Long>,
            JpaSpecificationExecutor<Sistematizacija> {

}
