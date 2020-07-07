package br.com.gliceu.conexao;

import java.sql.*;

public class ModuloConexao {

    public static Connection conect() {

        /* java.sql.Connection conexao = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/truck";
        String user = "root";
        String password = "";*/
        java.sql.Connection conexao = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://sql10.freemysqlhosting.net:3306/sql10349512";
        String user = "sql10349512";
        String password = "IWZMmHzTrQ";

        /* java.sql.Connection conexao = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://db4free.net:3306/truck_01?autoReconnect=true";
        String user = "gliceucamargo_01";
        String password = "kakagito3004";*/
//?userTimezone=true&serverTimezone=UTC"
/*Server: sql10.freemysqlhosting.net
 Name: sql10346787
 Username: sql10346787
 Password: xSKenxlWa6
 Port number: 3306*/
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            return null;
        }
    }

}
