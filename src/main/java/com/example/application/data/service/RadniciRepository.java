package com.example.application.data.service;

import com.example.application.data.entity.Radnici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RadniciRepository extends JpaRepository<Radnici, Long>, JpaSpecificationExecutor<Radnici> {

}
