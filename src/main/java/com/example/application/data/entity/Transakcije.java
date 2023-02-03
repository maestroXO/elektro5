package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Transakcije extends AbstractEntity {

    private Integer id_transakcije;
    private String naziv;
    private Integer zaduzenje;
    private Integer razduzenje;

    public Integer getId_transakcije() {
        return id_transakcije;
    }
    public void setId_transakcije(Integer id_transakcije) {
        this.id_transakcije = id_transakcije;
    }
    public String getNaziv() {
        return naziv;
    }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    public Integer getZaduzenje() {
        return zaduzenje;
    }
    public void setZaduzenje(Integer zaduzenje) {
        this.zaduzenje = zaduzenje;
    }
    public Integer getRazduzenje() {
        return razduzenje;
    }
    public void setRazduzenje(Integer razduzenje) {
        this.razduzenje = razduzenje;
    }

}
