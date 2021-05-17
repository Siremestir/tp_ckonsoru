package com.fges.ckonsoru.view;

import com.fges.ckonsoru.dao.RendezVousDAO;
import com.fges.ckonsoru.model.RendezVous;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ListeAnnulationsAction extends ActionConsole {
    protected RendezVousDAO rdvDao;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ListeAnnulationsAction(int numero, String description, RendezVousDAO rdvDAO) {

        super(numero, description);
        this.rdvDao = rdvDAO;
    }

    @Override
    public void executer(Scanner scanner) {
        List<RendezVous> rdvs = rdvDao.listeRendezVousAnnules();
        for (RendezVous rdv : rdvs){
            System.out.println(rdv.getNomClient() + " le " + dateTimeFormatter.format(rdv.getDate())+" (" + rdv.getDelai() + " avant)");
        }
    }
}
