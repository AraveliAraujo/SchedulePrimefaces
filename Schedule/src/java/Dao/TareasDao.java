package Dao;

import Models.Tareas;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TareasDao extends DAO {

    public List<Tareas> listar() throws SQLException, Exception { //Creamos un método listar
        this.Conectar(); //llamamos a la conexión para este método
        List<Tareas> lista; //Guardamos toda la lista dentro de una variable "lista"
        ResultSet rs; //Objeto para traer los datos de la BD
        try { //Try_Catch para el manejo de errores
            String sql = "Select CODIGO,convert(varchar(10), FECHA, 103) AS FECHA,DESCRIPCION,ESTADO from Tareas WHERE ESTADO LIKE 'A'"; //Guardamos la consulta en la variable sql
//            String sql = "Select CODIGO,TO_CHAR(FECHA,'DD/MM/YYYY') AS FECHA,DESCRIPCION,ESTADO from Tareas WHERE ESTADO LIKE 'A'"; //Consulta en caso de Usar la Base de Datos en Oracle
            PreparedStatement ps = this.getCn().prepareCall(sql); //Llamamos y preparamos la consulta con la variable SQL
            rs = ps.executeQuery(); // Ejecutamos la consulta
            lista = new ArrayList(); //Creamos un Array para mostrar los valores desde la variable lista
            Tareas model; // creamos una variable para el modelo
            while (rs.next()) { //Bucle "mientras" para traer los datos uno por uno
                model = new Tareas( //vamos guardando los valores en el model para ir jalando los datos
                        rs.getString("CODIGO"), // Jalamos el valor de la base de datos con el objeto "rs" del campo "CODIGO"
                        rs.getString("FECHA"), // Jalamos el valor de la base de datos con el objeto "rs" del campo "FECHA"
                        rs.getString("DESCRIPCION"), // Jalamos el valor de la base de datos con el objeto "rs" del campo "DESCRIPCIÓN"
                        rs.getString("ESTADO") // Jalamos el valor de la base de datos con el objeto "rs" del campo "ESTADO"
                );
                lista.add(model); //Agregamos la lista al modelo
            }
            return lista; //le pedimos que nos devuelva una lista ya que el método es tipo Lista
        } catch (SQLException e) { //Cerramos el manejo de errores
            throw e; //para mostrar el errore en caso de que lo hubiera
        }
    }

    public void actualizarFecha(String Codigo, String Fecha) throws Exception { //Creamos el método para actualizar la fecha con esos parámetros, esto sirve para mover los evento de un lado a otro
        this.Conectar(); //llamamos a la conexión para este método
        try { //Try_Catch para el manejo de errores
            String sql = "UPDATE TAREAS SET FECHA = (CONVERT(DATE,?, 103)) WHERE CODIGO LIKE ?"; //Guardamos la sentencia en una variablr tipo String llamada sql
//            String sql = "UPDATE TAREAS SET FECHA = TO_DATE(?,'DD/MM/YYYY') WHERE CODIGO LIKE ?"; //Consulta en caso de Usar la Base de Datos en Oracle
            PreparedStatement ps = this.getCn().prepareStatement(sql); //Llamamos y preparamos la consulta desde la variable SQL
            ps.setString(1, Fecha); //vamos guardando los datos
            ps.setString(2, Codigo);
            ps.executeUpdate(); //Ejecutamos la consulta para actualizar
        } catch (SQLException e) { //Cerramos el manejo de errores
            throw e; //Para mostrar el posible error
        }
    }

    public void actualizar(String Codigo, String Descripcion, String Fecha) throws Exception { //Método actualizar para modificar los datos cuando hacemos clic en un evento
        this.Conectar(); //llamamos a la conexión para este método
        try { //Try_Catch para el manejo de errores
            String sql = "UPDATE TAREAS SET FECHA = (CONVERT(DATE,?, 103)),DESCRIPCION=? WHERE CODIGO LIKE ?"; //Guardamos la sentencia en una variablr tipo String llamada sql
//             String sql = "UPDATE TAREAS SET FECHA = TO_DATE(?,'DD/MM/YYYY'),DESCRIPCION=? WHERE CODIGO LIKE ?";//Consulta en caso de Usar la Base de Datos en Oracle
            PreparedStatement ps = this.getCn().prepareStatement(sql); //Llamamos y preparamos la consulta desde la variable SQL
            ps.setString(1, Fecha); //vamos guardando los datos
            ps.setString(2, Descripcion); //vamos guardando los datos
            ps.setString(3, Codigo); //vamos guardando los datos
            ps.executeUpdate(); //Ejecutamos la sentencia para actualizar el registro    
        } catch (SQLException e) { //Cerramos el Try_Catch para el manejo de erroes
            throw e; //para mostrar en caso hubiera un error
        }
    }

    public void eliminar(String Codigo) throws Exception { //Método para eliminar un evento creado
        this.Conectar(); //Llamamos a la conexión en este método
        try { //Try_Catch para el manejo de errores
            String sql = "UPDATE TAREAS SET ESTADO = 'I' WHERE CODIGO = ?";//Guardamos la sentencia en una variablr tipo String llamada sql
//             String sql = "UPDATE TAREAS SET ESTADO = 'I' WHERE CODIGO = ?";//Consulta en caso de Usar la Base de Datos en Oracle
            PreparedStatement ps = this.getCn().prepareStatement(sql);//Llamamos y preparamos la consulta desde la variable SQL
            ps.setString(1, Codigo); //Usamos el código del evento para eliminarlo
            ps.executeUpdate(); //Ejecutamos la sentencia
        } catch (SQLException e) { //Cerramos el Try_Catch
            throw e; //Para mostrar un posible error
        }
    }

    public void ingresar(String Fecha, String Descripcion) throws Exception { //Creamos el meétodo ingresar con dos parámetros
        this.Conectar(); //Llamamos a la conexión en este método
        try {//Try_Catch para el manejo de errores
            String sql = "INSERT INTO TAREAS (FECHA,DESCRIPCION) VALUES (CONVERT(DATE,?, 103),?)";//Guardamos la sentencia en una variablr tipo String llamada sql
//            String sql = "INSERT INTO TAREAS (FECHA,DESCRIPCION) VALUES (TO_DATE(?,'DD/MM/YYYY'),?)";//Consulta en caso de Usar la Base de Datos en Oracle
            PreparedStatement ps = this.getCn().prepareStatement(sql);//Llamamos y preparamos la consulta desde la variable SQL
            ps.setString(1, Fecha); //Vamos guardando los valores en cada campo
            ps.setString(2, Descripcion);
            ps.executeUpdate(); //Ejecutamos la sentencia 
        } catch (SQLException e) { // cerramos el Try_Catch
            throw e; //Para mostrar posibles errores
        }
    }

}
