package Controller;

import Dao.TareasDao;
import Models.Tareas;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.jms.Message;
import lombok.Data;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import sun.security.jgss.spi.MechanismFactory;

@Data
@Named(value = "tareasController")
@SessionScoped
public class TareasController implements Serializable {

    DateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private List<Tareas> LstTareas = new ArrayList();
    private Tareas tarea = new Tareas();

    private ScheduleModel Calendario = new DefaultScheduleModel();
    private ScheduleEvent event = new DefaultScheduleEvent();

    private TareasDao dao = new TareasDao();

    @PostConstruct //Notación que define a un método como un método de iniciación
    public void start() { //método de iniciación que ejecuta el método listar que creamos posteriormente
        try { //para el manejo de errores
            listar(); // llamamos al método listar que creamos en la línea 56
        } catch (Exception ex) { // en caso de existir un error, ejecuta y muestra en consola el error
            Logger.getLogger(TareasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listar() throws Exception { // creamos el método listar que llamamos en el método Start
        try { // para el manejo de erroes
            LstTareas = dao.listar(); //guardamos los registros que se ejecutan en dao.listar en la variable "LstTareas"
            Calendario = new DefaultScheduleModel(); //Instanciamos el objeto del calendario
            for (Tareas tareas : LstTareas) { // la lista que tenemos en la BD la mostrando una por una (como algoritmo)
                ScheduleEvent evento = new DefaultScheduleEvent(tareas.getDescripcion(), DateFormat.parse(tareas.getFecha()), DateFormat.parse(tareas.getFecha()), tareas.getCodigo()); //estos son los datos que vamos a ir jalando en este bucle
                Calendario.addEvent(evento); //Es el encargado de ingresar los datos al modelo del calendar, de esta manera los va mostrando
            }
        } catch (SQLException e) { //cerramos el manejo de errores
            throw e; // por si haya un error, nos mostrará en esta variable
        }
    }

    public void actualizarEvento() throws Exception { //Update en el calendario (Cuando arrastramos un evento en el calendario por ej.)
        Calendar calendar = Calendar.getInstance(); //instanciamos un objeto calendar 
        calendar.setTime(event.getStartDate()); //Establecemos la fecha que hemos seleccionado 
        calendar.add(Calendar.DAY_OF_YEAR, 1); //Le sumamos un día más a la fecha seleccionada
        dao.actualizar(event.getStyleClass(), event.getTitle(), DateFormat.format(calendar.getTime())); //Ejecutamos el método actualizar en la Clase TareasDao
        listar(); // ejecutamos el método listar para mostrar los cambios de maner automática
        limpiar(); //Llamamos el método limpiar para eliminar los valores guardados en el modelo
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("CORRECTO", "ACTUALIZADO CORRECTAMENTE"));
    }

    public void ingresarEvento() throws Exception { //Creamos un método para ingresar un nuevo evento
        Calendar calendar = Calendar.getInstance(); //instanciamos el objeto calendar
        calendar.setTime(event.getStartDate()); //Establecemos la fecha seleccionada
        calendar.add(Calendar.DAY_OF_YEAR, 1); //Sumamos un día a la fecha que seleccionamos anteriormente
        dao.ingresar(DateFormat.format(calendar.getTime()), event.getTitle()); // llamamos al método ingresar de la clase TareasDao
        listar(); //Listamos para mostrar los cambios de manera automática
        limpiar(); //Llamamos al método limpiar para eliminar los valores asignados en el modelo
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("CORRECTO", "INGRESADO CORRECTAMENTE"));
    }

    public void eliminar() throws Exception { //Creamos un método para eliminar los eventos creados en el calendario
        dao.eliminar(event.getStyleClass()); //llamamos al método eliminar desde la clase TareasDao
        listar(); //Listamos para visualizar los cambios en el calendario
        limpiar(); //Llamamos al método limpiar para eliminar los valores asigandos en las variables del modelo
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ELIMINADO CORRECTAMENTE"));
    }

    public void limpiar() { //Creamos un método limpiar
        tarea = new Tareas(); //Volvemos a instanciar el objeto tarea para que todos su valores estén vacíos
    }

    public void onDateSelect(SelectEvent selectEvent) throws Exception { //Creamos un método para seleccionar la fecha y guardarlo en el objeto ""event
        event = new DefaultScheduleEvent(null, (Date) selectEvent.getObject(), (Date) selectEvent.getObject()); //la fecha seleccionada la guardamos en el modelo evento
    }

    public void onEventMove(ScheduleEntryMoveEvent event) throws Exception { //Usamos el método de primefaces para actualizar registros cuando movemos los eventos de un día a otro
        dao.actualizarFecha(event.getScheduleEvent().getStyleClass(), DateFormat.format(event.getScheduleEvent().getStartDate())); //llamamos al método actualizar de nuestro DAO
    }

    public void onEventSelect(SelectEvent selectEvent) { //Usamos el método para seleccionar un evento creado en nuestro calendario
        event = (ScheduleEvent) selectEvent.getObject(); //colocar los valores del event para mostrarlo en el dilogo
    }

}
