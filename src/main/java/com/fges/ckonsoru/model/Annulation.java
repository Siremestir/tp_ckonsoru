/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author julie.jacques
 */
public class Annulation {
    
    protected String nomClient;
    protected LocalDateTime creneau;
    protected String nomVeterinaire;
    protected LocalTime delai;

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public LocalDateTime getCreneau() {
        return creneau;
    }

    public void setCreneau(LocalDateTime creneau) {
        this.creneau = creneau;
    }

    public String getNomVeterinaire() {
        return nomVeterinaire;
    }

    public void setNomVeterinaire(String nomVeterinaire) {
        this.nomVeterinaire = nomVeterinaire;
    }

    public LocalTime getDelai() {
        return delai;
    }

    public void setDelai(LocalTime delai) {
        this.delai = delai;
    }
    
    
}
