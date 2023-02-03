package com.example.application.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;

@Entity
public class Arhiva_prometa extends AbstractEntity {

    private Integer id_radnika;
    private LocalDate datum;
    private Integer dokument;
    private String opis;
    private Integer zaduzenje;
    private Integer razduzenje;
    private LocalDateTime date;
    private Integer korisnik;
    private Integer cijena;
    private Integer neto;
    private Integer bruto;
    private Integer id_transakcije;
    private Integer id_opreme;

    public Integer getId_radnika() {
        return id_radnika;
    }
    public void setId_radnika(Integer id_radnika) {
        this.id_radnika = id_radnika;
    }
    public LocalDate getDatum() {
        return datum;
    }
    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
    public Integer getDokument() {
        return dokument;
    }
    public void setDokument(Integer dokument) {
        this.dokument = dokument;
    }
    public String getOpis() {
        return opis;
    }
    public void setOpis(String opis) {
        this.opis = opis;
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
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public Integer getKorisnik() {
        return korisnik;
    }
    public void setKorisnik(Integer korisnik) {
        this.korisnik = korisnik;
    }
    public Integer getCijena() {
        return cijena;
    }
    public void setCijena(Integer cijena) {
        this.cijena = cijena;
    }
    public Integer getNeto() {
        return neto;
    }
    public void setNeto(Integer neto) {
        this.neto = neto;
    }
    public Integer getBruto() {
        return bruto;
    }
    public void setBruto(Integer bruto) {
        this.bruto = bruto;
    }
    public Integer getId_transakcije() {
        return id_transakcije;
    }
    public void setId_transakcije(Integer id_transakcije) {
        this.id_transakcije = id_transakcije;
    }
    public Integer getId_opreme() {
        return id_opreme;
    }
    public void setId_opreme(Integer id_opreme) {
        this.id_opreme = id_opreme;
    }

}
