/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.view;

import com.fges.ckonsoru.dao.DisponibilitesDAO;
import com.fges.ckonsoru.model.Disponibilite;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    
    public AfficheCreneauxDateAction(int numero, String description, 
                                     DisponibilitesDAO dispoDAO) {
        super(numero, description);
        this.dispoDAO = dispoDAO;
    }

    @Override
    public void executer(Scanner scanner) {
        System.out.println("Entrer une date au format JJ/MM/AAAA (ex: 18/03/2021)");
        String sDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(sDate, dateFormatter);
        List<Disponibilite> dispos = dispoDAO.getDisponibilitesPourDate(date);
        System.out.println("Disponibilités pour le " + date.format(dateFormatter));
        for(Disponibilite dispo : dispos){
            System.out.println(dispo.getVeterinaire() + " : " + dateTimeFormatter.format(dispo.getDebut()));
        }
    }
    
}
