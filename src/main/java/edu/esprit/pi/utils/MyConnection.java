package edu.esprit.pi.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    private final String url="jdbc:mysql://localhost:3306/3A3";
    private final String Login="root";
    private final String Mdps ="";

    public static MyConnection instance;

    Connection cnx;

    public Connection getCnx() {
        return cnx;
    }

    private MyConnection(){
        try {
            cnx = DriverManager.getConnection(url,Login,Mdps);
            System.out.println("Connexion etablie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }
}
