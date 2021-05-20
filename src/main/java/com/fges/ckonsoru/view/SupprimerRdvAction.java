/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.view;

import com.fges.ckonsoru.observables.RendezVousSupprObservable;
import com.fges.ckonsoru.dao.ListeAttenteDAO;
import com.fges.ckonsoru.dao.RendezVousDAO;
import com.fges.ckonsoru.model.ListeAttente;
import com.fges.ckonsoru.model.RendezVous;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author julie.jacques
 */
public class SupprimerRdvAction 
    extends ActionConsole {

    protected RendezVousDAO rdvDao;
    protected ListeAttenteDAO laDao;
    protected DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    protected RendezVousSupprObservable observable;
    
    public SupprimerRdvAction(int numero, String description, RendezVousDAO rdvDao, ListeAttenteDAO laDao,
                              RendezVousSupprObservable observable) {
        super(numero, description);
        this.rdvDao = rdvDao;
        this.laDao = laDao;
        this.observable = observable;
    }

    @Override
    public void executer(Scanner scanner) {
        System.out.println("Suppression de rendez-vous");
        System.out.println("Indiquer une date et heure de début au format JJ/MM/AAAA HH:MM (ex: 18/03/2021 15:00)");
        String sDebut = scanner.nextLine();
        LocalDateTime debut = LocalDateTime.parse(sDebut, timeFormatter);
        System.out.println("Indiquer le nom du client");
        String client = scanner.nextLine();
        RendezVous delRdv = new RendezVous(client, debut, "");
        this.rdvDao.supprimerRendezVous(delRdv);
        System.out.println("Un rendez-vous pour " + client + " le  " + timeFormatter.format(debut) + " a été supprimé");
        observable.notifierObservateurs(delRdv);
    }
}
