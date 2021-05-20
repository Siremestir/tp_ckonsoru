package com.fges.ckonsoru.view;

import com.fges.ckonsoru.dao.ListeAttenteDAO;
import com.fges.ckonsoru.model.ListeAttente;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ListeAttenteAction extends ActionConsole{

    protected DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    protected ListeAttenteDAO laDao;
    public ListeAttenteAction(int numero, String description, ListeAttenteDAO laDao) {
        super(numero, description);
        this.laDao = laDao;
    }

    @Override
    public void executer(Scanner scanner) {
        System.out.println("Affichage de la liste d'attente");
        System.out.println("nom client (n°téléphone), créneau proposé, vétérinaire proposé");
        List<ListeAttente> attentes = laDao.listeAttenteEntiere();
        for (ListeAttente attente : attentes){
            if (attente.getCreneauPropose() == null){
                System.out.println(attente.getNomClient() + " ("
                        + attente.getNumTel() + "), -");
            } else {
                System.out.println(attente.getNomClient() + " ("
                        + attente.getNumTel() + "), "
                        + timeFormatter.format(attente.getCreneauPropose()) + ", "
                        + attente.getNomVeto());
            }
        }
    }
}
