package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Privilegije extends AbstractEntity {

    private Integer id_privilegije;
    private String naziv_role;
    private Integer insert;
    private Integer update;
    private Integer read;
    private Integer delete;
    private Integer all;

    public Integer getId_privilegije() {
        return id_privilegije;
    }
    public void setId_privilegije(Integer id_privilegije) {
        this.id_privilegije = id_privilegije;
    }
    public String getNaziv_role() {
        return naziv_role;
    }
    public void setNaziv_role(String naziv_role) {
        this.naziv_role = naziv_role;
    }
    public Integer getInsert() {
        return insert;
    }
    public void setInsert(Integer insert) {
        this.insert = insert;
    }
    public Integer getUpdate() {
        return update;
    }
    public void setUpdate(Integer update) {
        this.update = update;
    }
    public Integer getRead() {
        return read;
    }
    public void setRead(Integer read) {
        this.read = read;
    }
    public Integer getDelete() {
        return delete;
    }
    public void setDelete(Integer delete) {
        this.delete = delete;
    }
    public Integer getAll() {
        return all;
    }
    public void setAll(Integer all) {
        this.all = all;
    }

}
