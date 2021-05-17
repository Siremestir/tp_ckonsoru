/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.view;

import com.fges.ckonsoru.dao.DisponibilitesDAO;
import com.fges.ckonsoru.dao.RendezVousDAO;
import com.fges.ckonsoru.model.Disponibilite;
import com.fges.ckonsoru.model.RendezVous;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author julie.jacques
 */
public class InitApp extends ActionConsole {
    
    protected RendezVousDAO rdvDAO;
    protected DisponibilitesDAO dispoDAO;
    protected List<String> nomsClients = 
            Arrays.asList("Ackerman", "Arlelt","Blouse","Braun","Hoover",
                    "Jager","Kirschtein","Lenz","Reiss","Springer","Zoe", 
                    "Braus", "Smith","Bahner","Zacharias","Bossard","Langner");
    protected List<String> prenomsClients = 
            Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M",
                    "N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
    
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public InitApp(int numero, 
                   String description, 
                   RendezVousDAO rdvDAO,
                   DisponibilitesDAO dispoDAO) {
        super(numero, description);
        this.rdvDAO = rdvDAO;
        this.dispoDAO = dispoDAO;
    }
    
    @Override
    public void executer(Scanner scanner) {
        System.out.println("Remplissage automatique de semaine");
        System.out.println("Indiquer une date qui correspond à un lundi au format JJ/MM/AAAA (ex: 18/03/2021");
        String sDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(sDate, dateFormatter);
        // remplissage de 7 jours consécutifs...
        for(int i=0;i<7;i++){
            List<Disponibilite> dispos = dispoDAO.getDisponibilitesPourDate(date);
            // crée un rdv pour chaque dispo
            for (Disponibilite dispo : dispos ){
                RendezVous rdv = new RendezVous(genereClient(), dispo.getDebut(), dispo.getVeterinaire());
                rdvDAO.creerRendezVous(rdv);
            }
            date = date.plus(1,ChronoUnit.DAYS);
        }
    }
    
    private String genereClient(){
        String nom;
        nom = prenomsClients.get((int) (Math.random()*(prenomsClients.size()-1)));
        nom += ". " + nomsClients.get((int) (Math.random()*(nomsClients.size()-1)));
        return nom;
    }
    
    
}
