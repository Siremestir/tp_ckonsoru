package com.fges.ckonsoru.observables;

import com.fges.ckonsoru.observers.RendezVousSupprObserver;
import com.fges.ckonsoru.model.RendezVous;

import java.util.LinkedList;
import java.util.List;

public class RendezVousSupprObservableImpl implements RendezVousSupprObservable {

    List<RendezVousSupprObserver> observateurs = new LinkedList<>();

    @Override
    public void enregistrerObservateur(RendezVousSupprObserver observateur) {
        observateurs.add(observateur);
    }

    @Override
    public void supprimerObservateur(RendezVousSupprObserver observateur) {
        observateurs.remove(observateur);
    }

    @Override
    public void notifierObservateurs(RendezVous rendezVous) {
        for (RendezVousSupprObserver observateur : observateurs) {
            observateur.actualiser(rendezVous);
        }
    }
}
