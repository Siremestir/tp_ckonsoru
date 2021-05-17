/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.dao;

import com.fges.ckonsoru.model.RendezVous;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author julie.jacques
 */
public interface RendezVousDAO {
    
    /** 
     * Crée un rendez-vous et renvoie l'identifiant du rendez-vous créé
     * @param rendezVous
     */
    public void creerRendezVous(RendezVous rendezVous);
    
    
    /**
     * Supprime un rendez-vous à partir de son nom client + date + créneau
     * @param rendezVous 
     */
    public void supprimerRendezVous(RendezVous rendezVous);
    
    /**
     * Donne la liste des rendez-vous passés, présents et futurs 
     * pour un client donné
     * (récupération par ID ou match sur nom/prénom)
     * 
     * @param nomClient
     * @return 
     */
    public List<RendezVous> listeRendezVousPourClient(String nomClient);
    
    
    
    /**
     * Donne la liste des rendez-vous pour une date donnée
     * @param date
     * @return 
    **/
    public List<RendezVous> listeRendezVousPourDate(LocalDate date);


    /**
     * Donne la liste des rendez-vous annulés
     * @return
     **/
    public List<RendezVous> listeRendezVousAnnules();
}
