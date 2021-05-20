package com.fges.ckonsoru.observables;

import com.fges.ckonsoru.observers.RendezVousSupprObserver;
import com.fges.ckonsoru.model.RendezVous;

public interface RendezVousSupprObservable {

    public void enregistrerObservateur(RendezVousSupprObserver observateur);

    public void supprimerObservateur(RendezVousSupprObserver observateur);

    public void notifierObservateurs(RendezVous rendezVous);
}
