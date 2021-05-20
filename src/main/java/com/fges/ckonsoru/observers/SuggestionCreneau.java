package com.fges.ckonsoru.observers;

import com.fges.ckonsoru.dao.ListeAttenteDAO;
import com.fges.ckonsoru.model.ListeAttente;
import com.fges.ckonsoru.model.RendezVous;

import java.util.List;

public class SuggestionCreneau implements RendezVousSupprObserver {
    protected ListeAttenteDAO laDao;

    public SuggestionCreneau(ListeAttenteDAO laDao){
        this.laDao = laDao;
    }

    @Override
    public void actualiser(RendezVous rendezVous) {
        List<ListeAttente> listeAttentes = laDao.sortirListe(rendezVous.getDate().toLocalDate());
        if (!listeAttentes.isEmpty()) {
            laDao.suggererCreneau(rendezVous.getDate());
            System.out.println("Un client en liste d'attente sera rappelé.");
        }
    }
}
