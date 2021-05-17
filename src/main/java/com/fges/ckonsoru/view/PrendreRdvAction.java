/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.view;

import com.fges.ckonsoru.dao.RendezVousDAO;
import com.fges.ckonsoru.model.RendezVous;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author julie.jacques
 */
public class PrendreRdvAction
    extends ActionConsole {
    
    protected RendezVousDAO rdvDAO;
    protected DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PrendreRdvAction(int numero, String description, RendezVousDAO rdvDAO) {
        super(numero, description);
        this.rdvDAO = rdvDAO;
    }
    
    @Override
    public void executer(Scanner scanner) {
        System.out.println("Prise de rendez-vous");
        System.out.println("Indiquer une date et heure de début au format JJ/MM/AAAA HH:MM (ex: 18/03/2021 15:00)");
        String sDebut = scanner.nextLine();
        LocalDateTime debut = LocalDateTime.parse(sDebut, timeFormatter);
        System.out.println("Indiquer le nom du vétérinaire");
        String veto = scanner.nextLine();
        System.out.println("Indiquer le nom du client");
        String client = scanner.nextLine();
        RendezVous nouveauRdv = new RendezVous(client,debut,veto);
        rdvDAO.creerRendezVous(nouveauRdv);
        System.out.println("Un rendez-vous pour "+client+" avec " + veto + " a été réservé le " + timeFormatter.format(debut));
    }
}
