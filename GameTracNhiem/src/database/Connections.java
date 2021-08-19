package database;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class Connections {

    private static Connection getConnect() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            final String server = "jdbc:mysql://localhost/game";
            final String user = "root";
            final String pass = "";
            connection = DriverManager.getConnection(server, user, pass);
            if (connection == null) {
                System.out.println("Ket noi khong thanh cong");
            } else {
                System.out.println("Ket noi thanh cong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection Newconnect() {
        return getConnect();
    }
}
