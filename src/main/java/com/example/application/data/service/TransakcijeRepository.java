package com.example.application.data.service;

import com.example.application.data.entity.Transakcije;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransakcijeRepository extends JpaRepository<Transakcije, Long>, JpaSpecificationExecutor<Transakcije> {

}
