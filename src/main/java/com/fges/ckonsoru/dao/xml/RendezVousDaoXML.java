/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.dao.xml;

import com.fges.ckonsoru.dao.RendezVousDAO;
import com.fges.ckonsoru.model.RendezVous;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
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
public class RendezVousDaoXML
    extends DaoXML 
    implements RendezVousDAO {
    
    public RendezVousDaoXML(XmlDatabaseFile xdf) {
        super(xdf);
    }

    @Override
    public void creerRendezVous(RendezVous rendezVous) {
        Element rdv = this.xmlDbFile.xmldoc.createElement("rdv");
        Element debut = this.xmlDbFile.xmldoc.createElement("debut");
        debut.appendChild(this.xmlDbFile.xmldoc.createTextNode(rendezVous.getDate().format(DateTimeFormatter.ISO_DATE_TIME)));
        rdv.appendChild(debut);
        Element client = this.xmlDbFile.xmldoc.createElement("client");
        client.appendChild(this.xmlDbFile.xmldoc.createTextNode(rendezVous.getNomClient()));
        rdv.appendChild(client);
        Element veterinaire = this.xmlDbFile.xmldoc.createElement("veterinaire");
        veterinaire.appendChild(this.xmlDbFile.xmldoc.createTextNode(rendezVous.getVeterinaire()));
        rdv.appendChild(veterinaire);
        
        NodeList nodes = this.xmlDbFile.xmldoc.getElementsByTagName("rdvs");
        nodes.item(0).appendChild(rdv);
        
        this.xmlDbFile.writeXml();
        
    }

    @Override
    public void supprimerRendezVous(RendezVous rendezVous) {
        String sdate = rendezVous.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        try{
            String requeteXPATH = "/ckonsoru/rdvs/rdv[debut='"+sdate+"'][client='"+rendezVous.getNomClient()+"']";
            //System.out.println(requeteXPATH);
            
             // Create XPath object
            XPath xpath = XPathFactory.newInstance().newXPath();
            //create XPathExpression object
            XPathExpression expr =
                xpath.compile(requeteXPATH);
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(this.xmlDbFile.xmldoc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++){
                Node nNode = nodes.item(i);
                // TODO https://stackoverflow.com/questions/33548591/java-xpath-remove-element-from-xml
                nNode.getParentNode().removeChild(nodes.item(i));
            }
            this.xmlDbFile.writeXml();

        }catch(XPathExpressionException e){
            System.err.print("Pb pour récuperer les listeRendezVousPourDate (XML) : " + e.getMessage());
        }
    }

    @Override
    public List<RendezVous> listeRendezVousPourClient(String nomClient) {
        List<RendezVous> rdvs = new LinkedList<>();
        try{
            String requeteXPATH = "/ckonsoru/rdvs/rdv[client='"+nomClient+"']";
            //System.out.println(requeteXPATH);
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
                    rdvs.add(mapElementToRendezVous(eElement));
                }
            }
        }catch(XPathExpressionException e){
            System.err.print("Pb pour récuperer les listeRendezVousPourDate (XML) : " + e.getMessage());
        }
        return rdvs;
    }
    
    @Override
    public List<RendezVous> listeRendezVousPourDate(LocalDate date){
        List<RendezVous> rdvs = new LinkedList<>();
        String sdate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        try{
            String requeteXPATH = "/ckonsoru/rdvs/rdv[starts-with(debut,'"+sdate+"')]";
            System.out.println(requeteXPATH);
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
                    rdvs.add(mapElementToRendezVous(eElement));
                }
            }
        }catch(XPathExpressionException e){
            System.err.print("Pb pour récuperer les listeRendezVousPourDate (XML) : " + e.getMessage());
        }
        return rdvs;
    }

    @Override
    public List<RendezVous> listeRendezVousAnnules() {
        return null;
    }

    protected RendezVous mapElementToRendezVous(Element eElement){
        String debut = eElement.getElementsByTagName("debut").item(0).getTextContent();
        String client = eElement.getElementsByTagName("client").item(0).getTextContent();
        String veto = eElement.getElementsByTagName("veterinaire").item(0).getTextContent();
        LocalDateTime debutLD = 
                        LocalDateTime.parse(debut, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return new RendezVous(client,debutLD,veto);
    } 
}
