package com.example.application.data.service;

import com.example.application.data.entity.Privilegije;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PrivilegijeRepository extends JpaRepository<Privilegije, Long>, JpaSpecificationExecutor<Privilegije> {

}
