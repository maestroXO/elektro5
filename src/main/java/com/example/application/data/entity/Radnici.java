package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Radnici extends AbstractEntity {

    private Integer id_radnika;
    private String ime;
    private String prezime;

    public Integer getId_radnika() {
        return id_radnika;
    }
    public void setId_radnika(Integer id_radnika) {
        this.id_radnika = id_radnika;
    }
    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }
    public String getPrezime() {
        return prezime;
    }
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

}
