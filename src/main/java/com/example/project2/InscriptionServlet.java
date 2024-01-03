package com.example.project2;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class InscriptionServlet extends HttpServlet {

    // Méthode doPost pour gérer les requêtes POST
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer les paramètres du formulaire
        String nom = request.getParameter("NomClient");
        String prenom = request.getParameter("PrenClient");
        String mail = request.getParameter("MailClient");
        String tel = request.getParameter("TelClient");
        String motDePasse = request.getParameter("MotDePasse");

        // Connection à la base de données Oracle
        String jdbcURL = "jdbc:oracle:thin:@//localhost:1521/XE"; // Adapter URL, username, password
        String dbUser = "votre_Mail";
        String dbPassword = "votre_MotDePasse";
        Connection connection = new Connection(jdbcURL, dbUser, dbPassword) ;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            // Requête SQL pour insérer les données dans la table client
            String sql = "INSERT INTO hr.client (NumCLient, NomClient, PrenClient, MailClient, TelClient, MotDePasse) VALUES (SEQ_CLIENT.NEXTVAL, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Affecter les valeurs aux paramètres de la requête SQL
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, mail);
            statement.setString(4, tel);
            statement.setString(5, motDePasse);

            // Exécuter la requête d'insertion
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // Inscription réussie
                response.sendRedirect("index.jsp"); // Redirection vers une page de confirmation
            } else {
                // Échec de l'inscription
                response.sendRedirect("inscription.jsp"); // Redirection vers une page d'échec
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            // Fermer la connexion à la base de données
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
