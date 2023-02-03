package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Sistematizacija extends AbstractEntity {

    private String naziv_sektora;
    private String naziv_sluzbe;
    private Integer id_fizicke_lokacije;
    private String naziv_odjeljenja;
    private String ostalo;

    public String getNaziv_sektora() {
        return naziv_sektora;
    }
    public void setNaziv_sektora(String naziv_sektora) {
        this.naziv_sektora = naziv_sektora;
    }
    public String getNaziv_sluzbe() {
        return naziv_sluzbe;
    }
    public void setNaziv_sluzbe(String naziv_sluzbe) {
        this.naziv_sluzbe = naziv_sluzbe;
    }
    public Integer getId_fizicke_lokacije() {
        return id_fizicke_lokacije;
    }
    public void setId_fizicke_lokacije(Integer id_fizicke_lokacije) {
        this.id_fizicke_lokacije = id_fizicke_lokacije;
    }
    public String getNaziv_odjeljenja() {
        return naziv_odjeljenja;
    }
    public void setNaziv_odjeljenja(String naziv_odjeljenja) {
        this.naziv_odjeljenja = naziv_odjeljenja;
    }
    public String getOstalo() {
        return ostalo;
    }
    public void setOstalo(String ostalo) {
        this.ostalo = ostalo;
    }

}
