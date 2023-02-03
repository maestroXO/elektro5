package com.example.application.data.service;

import com.example.application.data.entity.Terenska_jedinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Terenska_jedinicaRepository
        extends
            JpaRepository<Terenska_jedinica, Long>,
            JpaSpecificationExecutor<Terenska_jedinica> {

}
