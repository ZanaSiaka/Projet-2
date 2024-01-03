package com.example.project2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "", value = "/inscription")
public class InscriptionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numClient = request.getParameter("numClient");
        String nomClient = request.getParameter("nomClient");
        String prenClient = request.getParameter("prenClient");
        String mailClient = request.getParameter("mailClient");
        String telClient = request.getParameter("telClient");
        String motDePasse = request.getParameter("motDePasse");

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Charger le pilote JDBC Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // URL de connexion à la base de données Oracle
            String jdbcUrl = "jdbc:oracle:thin:@//localhost:1521/hr"; // Assurez-vous d'ajuster le port et le nom de la base de données HR

            // Établir la connexion
            connection = DriverManager.getConnection(jdbcUrl, "votre_mail", "votre_mot_de_passe"); // Remplacez par votre nom d'utilisateur et mot de passe

            // Requête d'insertion pour insérer les données du client dans la table HR.CLIENTS (exemple)
            String insertQuery = "INSERT INTO HR.CLIENTS (NumClient, NomClient, PrenClient, MailClient, TelClient, MotDePasse) VALUES (?, ?, ?, ?, ?, ?)";

            // Créer la déclaration préparée
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, numClient);
            preparedStatement.setString(2, nomClient);
            preparedStatement.setString(3, prenClient);
            preparedStatement.setString(4, mailClient);
            preparedStatement.setString(5, telClient);
            preparedStatement.setString(6, motDePasse);

            // Exécuter la requête d'insertion
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Rediriger vers une page de confirmation en cas de succès
                response.sendRedirect("index.jsp");
            } else {
                // Gérer l'échec de l'insertion
                response.sendRedirect("inscription.jsp");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Gérer les exceptions appropriées
        } finally {
            // Fermer les ressources (connexion, déclaration préparée, etc.)
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Gérer les exceptions appropriées pour la fermeture des ressources
            }
        }
    }
}
