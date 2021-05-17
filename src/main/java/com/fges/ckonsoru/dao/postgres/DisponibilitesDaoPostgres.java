/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.dao.postgres;

import com.fges.ckonsoru.dao.DisponibilitesDAO;
import com.fges.ckonsoru.model.Disponibilite;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DisponibilitesDaoPostgres 
    extends DaoPostgres
    implements DisponibilitesDAO 
    {

    public DisponibilitesDaoPostgres(PostgresConnexion postgresConnexion) {
        super(postgresConnexion);
    }

    
    
    /**
     * Note : https://jdbc.postgresql.org/documentation/head/java8-date-time.html
     * @param date
     * @return 
     */
    @Override
    public List<Disponibilite> getDisponibilitesPourDate(LocalDate date) {
        List<Disponibilite> dispos = new ArrayList<>();
        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                    "WITH creneauxDisponibles AS \n" +
                    "	(SELECT vet_nom, generate_series(?::date+dis_debut,\n" +
                    "						   ?::date+dis_fin-'00:20:00'::time,\n" +
                    "						   '20 minutes'::interval) debut\n" +
                    "	FROM disponibilite\n" +
                    "		INNER JOIN veterinaire\n" +
                    "			ON veterinaire.vet_id = disponibilite.vet_id\n" +
                    "	WHERE dis_jour = EXTRACT('DOW' FROM ?::date)\n" +
                    "	ORDER BY vet_nom, dis_id),\n" +
                    "	creneauxReserves AS \n" +
                    "	(SELECT vet_nom, rv_debut debut\n" +
                    "	 FROM rendezvous\n" +
                    "		INNER JOIN veterinaire\n" +
                    "		ON veterinaire.vet_id = rendezvous.vet_id\n" +
                    "		WHERE rv_debut \n" +
                    "		BETWEEN ? \n" +
                    "		AND ? +'23:59:59'::time),\n" +
                    "	creneauxRestants AS\n" +
                    "	(SELECT * FROM creneauxDisponibles\n" +
                    "	EXCEPT\n" +
                    "	SELECT * FROM creneauxReserves)\n" +
                    "SELECT * FROM creneauxRestants\n" +
                    "ORDER BY vet_nom, debut");
            st.setObject(1, date);
            st.setObject(2, date);
            st.setObject(3, date);
            st.setObject(4, date);
            st.setObject(5, date);
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                Disponibilite dispo = new Disponibilite();
                dispo.setVeterinaire(rs.getString(1));
                dispo.setDebut((LocalDateTime) rs.getObject(2,LocalDateTime.class));
                dispos.add(dispo);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("Problème lors de la requête getDisponibilitesPourDate");
            System.err.println(ex.getMessage());
        }
        return dispos;
    }
    
}
