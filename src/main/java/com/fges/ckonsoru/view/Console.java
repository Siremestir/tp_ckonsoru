/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.view;
import com.fges.ckonsoru.dao.DisponibilitesDAO;
import com.fges.ckonsoru.dao.RendezVousDAO;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 *
 * @author julie.jacques
 */
public class Console {
    
    protected HashMap<Integer,ActionConsole> actionsParNumero;
    
    public Console(DisponibilitesDAO disponibilitesDAO,
                   RendezVousDAO rdvDAO){
        actionsParNumero = new HashMap<>();
        InitApp action0 =
                new InitApp(0,"Initialiser une semaine complète [DEV]",rdvDAO,disponibilitesDAO);
        actionsParNumero.put(0,action0);
        AfficheCreneauxDateAction action1 = 
            new AfficheCreneauxDateAction(1,"Afficher les créneaux disponibles pour une date donnée",disponibilitesDAO);
        actionsParNumero.put(1,action1);
        ListeRdvClientAction action2 = 
            new ListeRdvClientAction(2, "Lister les rendez-vous passés, présent et à venir d'un client",rdvDAO);
        actionsParNumero.put(2,action2);
        PrendreRdvAction action3 = 
            new PrendreRdvAction(3, "Prendre un rendez-vous",rdvDAO);
        actionsParNumero.put(3,action3);
        SupprimerRdvAction action4 =
            new SupprimerRdvAction(4, "Supprimer un rendez-vous",rdvDAO);
        actionsParNumero.put(4, action4);
        ListeAnnulationsAction action5 =
                new ListeAnnulationsAction(5, "Afficher les rendez-vous annulés",rdvDAO);
        actionsParNumero.put(5, action5);
    }
    
    public void afficheMenu(){
        System.out.println("Actions disponibles :");
        for (Entry<Integer,ActionConsole> eAction : actionsParNumero.entrySet()){
            System.out.println(eAction.getKey() + ": " + eAction.getValue().getDescription());
        }
        System.out.println("9. Quitter");
    }
    
    public void traiterAction(){
        Scanner scanner = new Scanner(System.in);
        String reponse = "";
        while(!reponse.equals("9")){
            if(!reponse.equals("")){
                ActionConsole action = actionsParNumero.get(Integer.parseInt(reponse));
                if(action!=null){
                    action.executer(scanner);
                }else{
                    System.out.println("Cette action n'existe pas");
                }
            }
            
            afficheMenu();
            System.out.println("Entrez un numéro d'action:");
            System.out.flush();
            reponse = scanner.nextLine();
        }
        
    }
}
