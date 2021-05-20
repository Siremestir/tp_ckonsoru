package com.fges.ckonsoru.dao.postgres;

import com.fges.ckonsoru.dao.ListeAttenteDAO;
import com.fges.ckonsoru.model.ListeAttente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ListeAttenteDaoPostgres extends DaoPostgres implements ListeAttenteDAO {

    public ListeAttenteDaoPostgres(PostgresConnexion postgresConnexion) {
        super(postgresConnexion);
    }

    private ListeAttente parseLa(ResultSet rs) throws SQLException {
        ListeAttente la = new ListeAttente(
                rs.getString("la_client"),
                rs.getString("la_numtel"),
                rs.getObject("la_creneaupropose", LocalDateTime.class));
        return la;
    }

    private ListeAttente parseLa(ResultSet rs, String option) throws SQLException {
        if (option.equals("n")){
            ListeAttente la = new ListeAttente(
                    rs.getString("la_client"),
                    rs.getString("la_numtel"),
                    rs.getObject("la_creneaupropose", LocalDateTime.class),
                    rs.getString(7));
            return la;
        }
        return parseLa(rs);
    }

    @Override
    public void ajouterAttente(ListeAttente listeAttente) {
        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                    "INSERT INTO listeattente (la_client, la_numtel, la_dateauplustard, la_datedemande)\n" +
                            "   VALUES(?,\n" +
                            "           ?,\n" +
                            "           ?," +
                            "           ?);	");
            st.setObject(1, listeAttente.getNomClient());
            st.setObject(2,listeAttente.getNumTel());
            st.setObject(3, listeAttente.getDateAuPlusTard());
            st.setObject(4, LocalDateTime.now());
            st.executeUpdate();

        }catch(SQLException e){
            System.err.println("Problème lors de la requête ajouterAttente");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<ListeAttente> sortirListe(LocalDate dateAuPlusTard) {
        List<ListeAttente> attentes = new ArrayList<>();

        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                    "SELECT la_client, la_numtel, la_creneaupropose\n" +
                            "FROM listeattente\n" +
                            "WHERE la_dateAuPlusTard \n" +
                            "		> ?\n" +
                            "   AND la_creneauPropose IS NULL;");
            st.setObject(1, dateAuPlusTard);
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                attentes.add(parseLa(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("Problème lors de la requête sortirListe");
            System.err.println(ex.getMessage());
        }
        return attentes;
    }

    @Override
    public void suggererCreneau(LocalDateTime creneau) {
        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                    "UPDATE listeattente \n" +
                            "SET la_creneaupropose = ?\n" +
                            "WHERE la_id = \n" +
                            "   (SELECT la_id FROM listeattente\n" +
                            "   WHERE ? <= la_dateAuPlusTard\n" +
                            "   AND la_creneaupropose IS NULL\n" +
                            "   ORDER BY la_datedemande ASC\n" +
                            "   LIMIT 1);");
            st.setObject(1, creneau);
            st.setObject(2, creneau);
            st.executeUpdate();

        }catch(SQLException e){
            System.err.println("Problème lors de la requête suggererCreneau");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<ListeAttente> listeAttenteEntiere() {
        List<ListeAttente> attentes = new ArrayList<>();

        try {
            PreparedStatement st = this.postgresConnexion.conn.prepareStatement(
                    "SELECT la_id, la_client, la_numTel, la_dateAuPlusTard, \n" +
                            "la_dateDemande, la_creneauPropose, vet_nom\n" +
                            "FROM listeAttente\n" +
                            "LEFT JOIN disponibilite\n" +
                            "ON dis_jour = EXTRACT('DOW' FROM la_creneauPropose)\n" +
                            "LEFT JOIN veterinaire\n" +
                            "ON veterinaire.vet_id = disponibilite.vet_id\n" +
                            "AND NOT EXISTS(\n" +
                            "SELECT 1 FROM rendezvous \n" +
                            "WHERE vet_id = disponibilite.vet_id\n" +
                            "AND la_creneauPropose = rv_debut)\n" +
                            "WHERE la_creneaupropose is NULL \n" +
                            "OR vet_nom IS NOT NULL;;");

            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                attentes.add(parseLa(rs, "n"));
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("Problème lors de la requête listeAttenteEntiere");
            System.err.println(ex.getMessage());
        }
        return attentes;
    }
}
