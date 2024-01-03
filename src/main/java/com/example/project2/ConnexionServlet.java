package com.example.project2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

public class ConnexionServlet extends HttpServlet {

    private DatabaseMetaData DatabaseConnection;

    // Méthode pour valider les informations d'identification dans votre base de données
    private boolean validateUser(String emailClient, String MotDePasse) {
        boolean isValid = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Établir une connexion à la base de données
            connection = DatabaseConnection.getConnection();

            // Requête pour vérifier les informations d'identification
            String query = "SELECT * FROM Client WHERE emailClient=? AND MotDePasse=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, emailClient);
            statement.setString(2, MotDePasse);

            // Exécuter la requête
            resultSet = statement.executeQuery();

            // Vérifier si un enregistrement correspondant est trouvé
            if (resultSet.next()) {
                // Si un enregistrement est trouvé, les informations d'identification sont valides
                isValid = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources ouvertes (ResultSet, PreparedStatement, Connection)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isValid;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, javax.servlet.ServletException, IOException {
        String emailClient = request.getParameter("emailClient");
        String MotDePasse = request.getParameter("MotDePasse");

        // Vérifier les informations d'identification dans votre base de données
        boolean authenticationResult = validateUser(emailClient, MotDePasse);

        if (authenticationResult) {
            // Si les informations sont valides, créer une session
            HttpSession session = request.getSession();
            session.setAttribute("emailClient", emailClient);

            // Rediriger l'utilisateur vers une page d'accueil (par exemple home.jsp)
            response.sendRedirect("home.jsp");
        } else {
            // Si les informations sont invalides, afficher un message d'erreur
            request.setAttribute("errorMessage", "Email ou mot de passe incorrect");
            RequestDispatcher dispatcher = request.getRequestDispatcher("connexion.jsp");
            try {
                dispatcher.forward((ServletRequest) request, (ServletResponse) response);
            } catch (javax.servlet.ServletException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

