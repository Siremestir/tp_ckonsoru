package com.fges.ckonsoru.observers;

import com.fges.ckonsoru.dao.RendezVousDAO;
import com.fges.ckonsoru.model.RendezVous;
import com.fges.ckonsoru.observers.RendezVousSupprObserver;

import java.time.Duration;
import java.time.LocalDateTime;

public class TracageAnnulation implements RendezVousSupprObserver {

    protected RendezVousDAO rdvDao;

    public TracageAnnulation(RendezVousDAO rdvDao){
        this.rdvDao = rdvDao;
    }

    @Override
    public void actualiser(RendezVous rendezVous) {
        Duration delai = Duration.between(LocalDateTime.now(), rendezVous.getDate());
        if (delai.minusDays(1).isNegative() && rendezVous.getDate().isAfter(LocalDateTime.now())) {
            rdvDao.enregistrerAnnulation(rendezVous);
            System.out.println("L'annulation a été tracée");
        }
    }
}
