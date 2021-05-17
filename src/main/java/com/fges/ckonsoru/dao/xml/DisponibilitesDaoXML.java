/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.dao.xml;

import com.fges.ckonsoru.dao.DisponibilitesDAO;
import com.fges.ckonsoru.model.Disponibilite;
import com.fges.ckonsoru.model.RendezVous;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author julie.jacques
 */
public class DisponibilitesDaoXML 
    extends DaoXML 
    implements DisponibilitesDAO {

    protected RendezVousDaoXML rdvDao;
    
    public DisponibilitesDaoXML(XmlDatabaseFile xdf, RendezVousDaoXML rdvDao) {
        super(xdf);
        this.rdvDao = rdvDao;
    }

    @Override
    public List<Disponibilite> getDisponibilitesPourDate(LocalDate date) {
        
        List<Disponibilite> dispos = new LinkedList<>();
        List<Disponibilite> disposFiltrees = new LinkedList<>();
        
        // convertit la date en jour francais
        String jour = date.format(DateTimeFormatter.ofPattern("EEEE",Locale.FRENCH));
        
        try{
            String requeteXPATH = "/ckonsoru/disponibilites/disponibilite[jour='"+jour+"']";
            // Create XPath object
            XPath xpath = XPathFactory.newInstance().newXPath();
            //create XPathExpression object
            XPathExpression expr =
                xpath.compile(requeteXPATH);
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(this.xmlDbFile.xmldoc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++){
                Node nNode = nodes.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String veto = eElement.getElementsByTagName("veterinaire").item(0).getTextContent();
                    String debut = eElement.getElementsByTagName("debut").item(0).getTextContent();
                    LocalDateTime debutLD = 
                            LocalDateTime.of(date, 
                                    LocalTime.parse(debut, DateTimeFormatter.ISO_LOCAL_TIME));
                    String fin = eElement.getElementsByTagName("fin").item(0).getTextContent();
                    LocalDateTime finLD = 
                            LocalDateTime.of(date, 
                                    LocalTime.parse(fin, DateTimeFormatter.ISO_LOCAL_TIME));
                    // genere les dispos par sauts de 20 minutes
                    dispos.addAll(genDisponibilitesCreneauxIntermediaires(debutLD,finLD,veto));
                }
            }
            
            // enlever les rdv déjà pris
            List<RendezVous> rdvs = rdvDao.listeRendezVousPourDate(date);
            
            // pour chaque rdv, enlever le créneau correspondant
            for(Disponibilite dispo : dispos){
                boolean garder = true;
                for(RendezVous rdv : rdvs){
                    if(rdv.getDate().compareTo(dispo.getDebut())==0){
                        garder=false;
                        break;
                    }
                }
                if(garder){
                    disposFiltrees.add(dispo);
                }
            }
                
        }catch(XPathExpressionException e){
            System.err.println("Pb au requêtage XML " + e.getMessage());
        }    
        
        return disposFiltrees;
    }
    
    /**
     * Génère tous les créneaux intermédiaires d'une date, espacés de 20minutes
     * 
     * ex: genDisponibilitesCreneauxIntermediaires("08:00","12:00") -> "08:00"-"08:20"; "08:20"-"08:40"...
     * @return 
     */
    private List<Disponibilite> genDisponibilitesCreneauxIntermediaires(
            LocalDateTime debut,
            LocalDateTime fin,
            String veterinaire){
        List<Disponibilite> dispos = new LinkedList<>();
        LocalDateTime creneauDebut = debut;
        while(creneauDebut.compareTo(fin) < 0 ){
            Disponibilite dispo = new Disponibilite();
            dispo.setDebut(creneauDebut);
            dispo.setVeterinaire(veterinaire);
            dispos.add(dispo);
            creneauDebut = creneauDebut.plus(20,ChronoUnit.MINUTES);
        }
        return dispos;
    }

}
