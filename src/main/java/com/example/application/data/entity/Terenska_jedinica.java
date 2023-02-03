package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Terenska_jedinica extends AbstractEntity {

    private Integer id_terenske_jedinice;
    private String naziv;

    public Integer getId_terenske_jedinice() {
        return id_terenske_jedinice;
    }
    public void setId_terenske_jedinice(Integer id_terenske_jedinice) {
        this.id_terenske_jedinice = id_terenske_jedinice;
    }
    public String getNaziv() {
        return naziv;
    }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

}
