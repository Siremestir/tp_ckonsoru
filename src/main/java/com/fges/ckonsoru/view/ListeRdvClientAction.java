/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.view;

import com.fges.ckonsoru.dao.RendezVousDAO;
import com.fges.ckonsoru.model.RendezVous;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author julie.jacques
 */
public class ListeRdvClientAction 
    extends ActionConsole {
    
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    protected RendezVousDAO rdvDao;
    
    public ListeRdvClientAction(int numero, String description, RendezVousDAO rdvDao) {
        super(numero, description);
        this.rdvDao = rdvDao;
    }

    @Override
    public void executer(Scanner scanner) {
        System.out.println("Affichage des rendez-vous d'un client");
        System.out.println("Indiquer le nom du client");
        String client = scanner.nextLine();
        List<RendezVous> rdvs = rdvDao.listeRendezVousPourClient(client);
        System.out.println(rdvs.size() + " rendez-vous trouvé(s) pour " + client);
        for (RendezVous rdv : rdvs){
            System.out.println(dateTimeFormatter.format(rdv.getDate())+" avec "+rdv.getVeterinaire());
        }
        
    }
   
}
