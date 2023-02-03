package com.example.application.data.service;

import com.example.application.data.entity.Arhiva_prometa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Arhiva_prometaRepository
        extends
            JpaRepository<Arhiva_prometa, Long>,
            JpaSpecificationExecutor<Arhiva_prometa> {

}
