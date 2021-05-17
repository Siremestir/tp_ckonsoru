/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fait la connexion à Postgres 
 * @author julie.jacques
 */
public final class PostgresConnexion {
    
    private static PostgresConnexion instance = null;
    protected Connection conn = null; 
    
    private PostgresConnexion(Properties appProps) throws SQLException{
        String url = appProps.getProperty("bdd.url");
        Properties props = new Properties();
        props.setProperty("user",appProps.getProperty("bdd.login"));
        props.setProperty("password",appProps.getProperty("bdd.mdp"));
        props.setProperty("ssl","false");
        this.conn = DriverManager.getConnection(url, props);
    }
    
    public final static PostgresConnexion getInstance(Properties appProps) throws SQLException{
        if (PostgresConnexion.instance == null){
            synchronized(PostgresConnexion.class) {
              if (PostgresConnexion.instance == null) {
                  PostgresConnexion.instance = new PostgresConnexion(appProps);
              }
            }
        }
        return PostgresConnexion.instance;
    }
    
}
