package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Fizicka_lokacija extends AbstractEntity {

    private Integer id_fizicke_lokacije;
    private String naziv_fizicke_lokacije;
    private Integer id_terenske_jedinice;

    public Integer getId_fizicke_lokacije() {
        return id_fizicke_lokacije;
    }
    public void setId_fizicke_lokacije(Integer id_fizicke_lokacije) {
        this.id_fizicke_lokacije = id_fizicke_lokacije;
    }
    public String getNaziv_fizicke_lokacije() {
        return naziv_fizicke_lokacije;
    }
    public void setNaziv_fizicke_lokacije(String naziv_fizicke_lokacije) {
        this.naziv_fizicke_lokacije = naziv_fizicke_lokacije;
    }
    public Integer getId_terenske_jedinice() {
        return id_terenske_jedinice;
    }
    public void setId_terenske_jedinice(Integer id_terenske_jedinice) {
        this.id_terenske_jedinice = id_terenske_jedinice;
    }

}
