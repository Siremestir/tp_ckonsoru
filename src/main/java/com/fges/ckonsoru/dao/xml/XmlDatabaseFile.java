/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.dao.xml;

import java.io.IOException;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author julie.jacques
 */
public class XmlDatabaseFile {
    
    private static XmlDatabaseFile instance = null;
    private static String filepath = "";
    protected Document xmldoc = null; 
    
    protected XmlDatabaseFile(Properties appProps){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            this.filepath = "src/main/resources/" + appProps.getProperty("xml.fichier");
            xmldoc = builder.parse(this.filepath);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.err.println("Erreur à l'ouverture de la bdd xml :  " + filepath);
            e.printStackTrace(System.err);
        }
    }
    
    /**
     * Writes the XML files 
     */
    public void writeXml(){
        
        DOMSource source = new DOMSource(this.xmldoc);
        
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(this.filepath);
            transformer.transform(source, result);
            
        }catch(TransformerException e){
            System.err.println("Problème lors de la sauvegarde du fichier XML");
        }
    }
    
    public static XmlDatabaseFile getInstance(Properties appProps){
        if(XmlDatabaseFile.instance == null){
            XmlDatabaseFile.instance = new XmlDatabaseFile(appProps);
        }
        return XmlDatabaseFile.instance;
        
    }
}
