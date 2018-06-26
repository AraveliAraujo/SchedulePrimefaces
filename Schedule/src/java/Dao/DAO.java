package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {

    private Connection cn;

    public void Conectar() throws Exception {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            cn = DriverManager.getConnection("jdbc:sqlserver://sql.jcondori.com:1433;databaseName=Schedule;user=root;password=root");
            if (cn != null) {
                System.out.println("Conectado");
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void Cerrar() throws SQLException {      //Cerrar la coneccion
        if (cn != null) {
            if (cn.isClosed() == false) {
                cn.close();
            }
        }
    }

    public Connection getCn() {
        return cn;
    }

    public void setCn(Connection cn) {
        this.cn = cn;
    }

    public static void main(String[] args) throws Exception {
        DAO dao = new DAO();
        dao.Conectar();
        if (dao.getCn() != null) {
            System.out.println("Conectado");
        } else {
            System.out.println("error");
        }
    }

}
