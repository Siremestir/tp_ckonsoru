/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru;

import com.fges.ckonsoru.dao.DisponibilitesDAO;
import com.fges.ckonsoru.dao.ListeAttenteDAO;
import com.fges.ckonsoru.dao.RendezVousDAO;
import com.fges.ckonsoru.dao.postgres.DisponibilitesDaoPostgres;
import com.fges.ckonsoru.dao.postgres.ListeAttenteDaoPostgres;
import com.fges.ckonsoru.dao.postgres.PostgresConnexion;
import com.fges.ckonsoru.dao.postgres.RendezVousDaoPostgres;
import com.fges.ckonsoru.dao.xml.DisponibilitesDaoXML;
import com.fges.ckonsoru.dao.xml.RendezVousDaoXML;
import com.fges.ckonsoru.dao.xml.XmlDatabaseFile;
import com.fges.ckonsoru.observables.RendezVousSupprObservableImpl;
import com.fges.ckonsoru.observers.SuggestionCreneau;
import com.fges.ckonsoru.observers.TracageAnnulation;
import com.fges.ckonsoru.view.Console;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Launch the App
 * @author julie.jacques
 */
public class App {
    
    private static final String PERSISTENCE_BDD = "bdd";
    private static final String PERSISTENCE_XML = "xml";
    
    public static void main(String args[]){
        
        System.out.println("Bienvenue sur Clinique Konsoru !");
        
        // chargement de la configuration de la persistence
        ConfigLoader cf = new ConfigLoader();
        Properties properties = cf.getProperties();
        System.out.println("Mode de persistence : "
                +properties.getProperty("persistence"));
        
        // init des DAO
        DisponibilitesDAO disponibilitesDAO=null;
        RendezVousDAO rdvDAO=null;
        ListeAttenteDAO laDAO = null;
        
        // init DAO postgres
        if(properties.getProperty("persistence").compareTo(PERSISTENCE_BDD)==0){
            try{
                PostgresConnexion pgConn = PostgresConnexion.getInstance(properties);
                disponibilitesDAO = new DisponibilitesDaoPostgres(pgConn);
                rdvDAO = new RendezVousDaoPostgres(pgConn);
                laDAO = new ListeAttenteDaoPostgres(pgConn);
                
            }catch(SQLException sqle){
                System.err.println("Problème de connexion à la base de données " + sqle.getMessage());
            }
        }
        
        // init DAO XML
        if(properties.getProperty("persistence").compareTo(PERSISTENCE_XML)==0){
            XmlDatabaseFile xdf = XmlDatabaseFile.getInstance(properties);
            rdvDAO = new RendezVousDaoXML(xdf);
            disponibilitesDAO = new DisponibilitesDaoXML(xdf,(RendezVousDaoXML) rdvDAO); 
        }

        // init observable et observateurs
        RendezVousSupprObservableImpl observable = new RendezVousSupprObservableImpl();
        TracageAnnulation tracageAnnulation = new TracageAnnulation(rdvDAO);
        SuggestionCreneau suggestionCreneau = new SuggestionCreneau(laDAO);

        // ajout des observateurs
        observable.enregistrerObservateur(tracageAnnulation);
        observable.enregistrerObservateur(suggestionCreneau);
         
        // lancement de la console
        Console console = new Console(disponibilitesDAO, rdvDAO, laDAO, observable);
        console.traiterAction();
        
        // fermeture de l'appli
        if(properties.getProperty("persistence").compareTo(PERSISTENCE_BDD)==0){
            try{
                PostgresConnexion pgConn = PostgresConnexion.getInstance(properties);
            }catch(SQLException sqle){
                System.err.println("Problème de fermeture de connexion à la base de données " + sqle.getMessage());
            }
        }
    }
    
}
