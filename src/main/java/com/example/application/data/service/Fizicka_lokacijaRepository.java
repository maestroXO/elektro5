package com.example.application.data.service;

import com.example.application.data.entity.Fizicka_lokacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Fizicka_lokacijaRepository
        extends
            JpaRepository<Fizicka_lokacija, Long>,
            JpaSpecificationExecutor<Fizicka_lokacija> {

}
