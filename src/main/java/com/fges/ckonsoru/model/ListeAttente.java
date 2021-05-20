/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author julie.jacques
 */
public class ListeAttente {
    
    protected int id;
    protected String nomClient;
    protected String numTel;
    protected LocalDate dateAuPlusTard;
    protected LocalDateTime creneauPropose;
    protected String nomVeto;

    public ListeAttente(String nomClient, String numTel, LocalDateTime creneauPropose){
        this.nomClient = nomClient;
        this.numTel = numTel;
        this.creneauPropose = creneauPropose;
    }

    public ListeAttente(String nomClient, String numTel, LocalDate dateAuPlusTard){
        this.nomClient = nomClient;
        this.numTel = numTel;
        this.dateAuPlusTard = dateAuPlusTard;
    }

    public ListeAttente(String nomClient, String numTel, LocalDateTime creneauPropose, String nomVeto){
        this.nomClient = nomClient;
        this.numTel = numTel;
        this.creneauPropose = creneauPropose;
        this.nomVeto = nomVeto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreneauPropose() {
        return creneauPropose;
    }

    public void setCreneauPropose(LocalDateTime creneauPropose) {
        this.creneauPropose = creneauPropose;
    }
    
    
    
    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public LocalDate getDateAuPlusTard() {
        return dateAuPlusTard;
    }

    public void setDateAuPlusTard(LocalDate dateAuPlusTard) {
        this.dateAuPlusTard = dateAuPlusTard;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getNomVeto() {
        return nomVeto;
    }

    public void setNomVeto(String nomVeto) {
        this.nomVeto = nomVeto;
    }
    
}
