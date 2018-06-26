package Models;

import lombok.Data;

@Data
public class Tareas {

    private String Codigo;
    private String Fecha;
    private String Descripcion;
    private String Estado;

    public Tareas() {
    }
    
    public Tareas(String Codigo, String Fecha, String Descripcion, String Estado) {
        this.Codigo = Codigo;
        this.Fecha = Fecha;
        this.Descripcion = Descripcion;
        this.Estado = Estado;
    }

}
