package com.fges.ckonsoru.dao;

import com.fges.ckonsoru.model.ListeAttente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ListeAttenteDAO {

    /**
     * Ajoute un élément à la liste d'attente
     * @param listeAttente
     */
    public void ajouterAttente(ListeAttente listeAttente);

    public List<ListeAttente> sortirListe(LocalDate dateAuPlusTard);

    public void suggererCreneau(LocalDateTime creneau);

    public List<ListeAttente> listeAttenteEntiere();
}
