/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.view;

import com.fges.ckonsoru.dao.DisponibilitesDAO;
import com.fges.ckonsoru.dao.ListeAttenteDAO;
import com.fges.ckonsoru.model.Disponibilite;
import com.fges.ckonsoru.model.ListeAttente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 *
 * @author julie.jacques
 */
public class AfficheCreneauxDateAction
    extends ActionConsole {
    
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    
    protected DisponibilitesDAO dispoDAO;
    protected ListeAttenteDAO laDAO;
    
    public AfficheCreneauxDateAction(int numero, String description, 
                                     DisponibilitesDAO dispoDAO,
                                     ListeAttenteDAO laDAO) {
        super(numero, description);
        this.dispoDAO = dispoDAO;
        this.laDAO = laDAO;
    }

    @Override
    public void executer(Scanner scanner) {
        System.out.println("Entrer une date au format JJ/MM/AAAA (ex: 18/03/2021)");
        String sDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(sDate, dateFormatter);
        List<Disponibilite> dispos = dispoDAO.getDisponibilitesPourDate(date);
        if (!dispos.isEmpty()){
            System.out.println("Disponibilités pour le " + date.format(dateFormatter));
            for(Disponibilite dispo : dispos){
                System.out.println(dispo.getVeterinaire() + " : " + dateTimeFormatter.format(dispo.getDebut()));
            }
        } else {
            System.out.println("Pas de disponibilités pour le " + sDate);
            System.out.println("Appuyez sur 1 pour vous inscrire en liste d'attente, 0 pour retourner au menu principal");
            String sChoix = scanner.nextLine();
            if (sChoix.equals("1")){
                System.out.println("Indiquez votre nom (ex: P. Smith)");
                String nom = scanner.nextLine();
                System.out.println("Indiquez un numéro auquel on pourra vous rappeler (ex:+33612345678)");
                String numero = scanner.nextLine();
                ListeAttente listeAttente = new ListeAttente(nom, numero, date);
                laDAO.ajouterAttente(listeAttente);
            }
        }

    }
    
}
