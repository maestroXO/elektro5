package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Oprema extends AbstractEntity {

    private Integer id_opreme;
    private String naziv_opreme;
    private Integer id_jm;

    public Integer getId_opreme() {
        return id_opreme;
    }
    public void setId_opreme(Integer id_opreme) {
        this.id_opreme = id_opreme;
    }
    public String getNaziv_opreme() {
        return naziv_opreme;
    }
    public void setNaziv_opreme(String naziv_opreme) {
        this.naziv_opreme = naziv_opreme;
    }
    public Integer getId_jm() {
        return id_jm;
    }
    public void setId_jm(Integer id_jm) {
        this.id_jm = id_jm;
    }

}
