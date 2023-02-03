package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Jm extends AbstractEntity {

    private Integer id_jm;
    private String naziv;

    public Integer getId_jm() {
        return id_jm;
    }
    public void setId_jm(Integer id_jm) {
        this.id_jm = id_jm;
    }
    public String getNaziv() {
        return naziv;
    }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

}
