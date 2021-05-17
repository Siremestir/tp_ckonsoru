/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.dao.postgres;

import com.fges.ckonsoru.dao.RendezVousDAO;
import com.fges.ckonsoru.model.RendezVous;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author julie.jacques
 */
public class RendezVousDaoPostgres 
    extends DaoPostgres
    implements RendezVousDAO {

    public RendezVousDaoPostgres(PostgresConnexion postgresConnexion) {
        super(postgresConnexion);
    }

    @Override
    public void creerRendezVous(RendezVous rendezVous) {
        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                "INSERT INTO rendezvous (vet_id, rv_debut, rv_client)\n" +
                "   VALUES((SELECT vet_id FROM veterinaire WHERE vet_nom = ?),\n" +
                "           ?,\n" +
                "           ?);	");
            st.setObject(1,rendezVous.getVeterinaire());
            st.setObject(2, rendezVous.getDate());
            st.setObject(3, rendezVous.getNomClient());
            st.executeUpdate();
            
        }catch(SQLException e){
            System.err.println("Problème lors de la requête creerRendezVous");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void supprimerRendezVous(RendezVous rendezVous) {
        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                "DELETE FROM rendezvous \n" +
                "WHERE rv_client = ?\n" +
                "AND rv_debut = ? ;");
            st.setObject(1,rendezVous.getNomClient());
            st.setObject(2, rendezVous.getDate());
            st.executeUpdate();
            
        }catch(SQLException e){
            System.err.println("Problème lors de la requête supprimerRendezVous");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<RendezVous> listeRendezVousPourClient(String nomClient) {
    
        List<RendezVous> rdvs = new ArrayList<>();
        
        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                    "SELECT rv_id, rv_debut, rv_client, vet_nom \n" +
                    "FROM rendezvous\n" +
                    "   INNER JOIN veterinaire \n"+
                    "       ON veterinaire.vet_id = rendezvous.vet_id \n"+
                    "WHERE rv_client = ?\n" +
                    "ORDER BY rv_debut DESC;");
            st.setObject(1, nomClient);
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                rdvs.add(parseRdv(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("Problème lors de la requête listeRendezVousPourClient");
            System.err.println(ex.getMessage());
        }
        return rdvs;
    }

    @Override
    public List<RendezVous> listeRendezVousPourDate(LocalDate date) {
        List<RendezVous> rdvs = new ArrayList<>();
        
        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                    "SELECT rv_id, rv_debut, rv_client\n" +
                    "FROM rendezvous\n" +
                    "WHERE rv_debut \n" +
                    "		BETWEEN ? \n" +
                    "		AND ? +'23:59:59'::time \n" +
                    "ORDER BY rv_debut DESC;");
            st.setObject(1, date);
            st.setObject(2, date);
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                rdvs.add(parseRdv(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("Problème lors de la requête listeRendezVousPourDate");
            System.err.println(ex.getMessage());
        }
        return rdvs;
    }

    @Override
    public List<RendezVous> listeRendezVousAnnules() {
        List<RendezVous> rdvs = new ArrayList<>();

        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                    "SELECT ann_client, ann_creneau, ann_delai, vet_nom\n" +
                            "FROM annulation\n" +
                            "INNER JOIN veterinaire \n" +
                            "       ON veterinaire.vet_id = annulation.vet_id \n"+
                            "ORDER BY ann_creneau DESC;");
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                rdvs.add(parseRdv(rs, "a"));
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("Problème lors de la requête listeRendezVousAnnules");
            System.err.println(ex.getMessage());
        }
        return rdvs;
    }

    /**
     * Parse un rendezvous contenu dans un resultset (rv_client, rv_debut, vet_nom)
     * @param rs
     * @return
     * @throws SQLException 
     */
    private RendezVous parseRdv(ResultSet rs) throws SQLException {
        RendezVous rdv = new RendezVous(
                rs.getString("rv_client"),
                (LocalDateTime) rs.getObject("rv_debut",LocalDateTime.class),
                rs.getString("vet_nom"));
        return rdv;
    }

    private RendezVous parseRdv(ResultSet rs, String option) throws SQLException {
        RendezVous rdv = null;
        if (option.equals("a")) {
            rdv = new RendezVous(
                    rs.getString("ann_client"),
                    (LocalDateTime) rs.getObject("ann_creneau", LocalDateTime.class),
                    rs.getString("vet_nom"),
                    (LocalTime)rs.getObject("ann_delai", LocalTime.class));
        }
        return rdv;
    }
}
