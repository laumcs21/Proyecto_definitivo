package co.edu.uniquindio.poo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Administrador {

    private static Administrador instancia;
    private String usuario;
    private String contraseña;
    public List<Cupon> cupones;
    public List<Evento> eventos;

    private Administrador() {

        this.usuario = ("admin@unieventos.com");
        this.contraseña = ("admin123");
        this.cupones = new ArrayList<>();
    }

    public static Administrador obtenerInstancia() {
        // Verificar si la instancia ya ha sido creada
        if (instancia == null) {
            // Si no ha sido creada, se crea una nueva instancia
            instancia = new Administrador();
        }
        // Retornar la instancia única
        return instancia;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public List<Cupon> getCupones() {
        return cupones;
    }

    public void setCupones(List<Cupon> cupones) {
        this.cupones = cupones;
    }

    public Cupon crearCupon(String nombreCupon, String codigo, LocalDate fecha, int porcentajeDescuento) {
        return new Cupon(nombreCupon, codigo, fecha, porcentajeDescuento);
    }

    public List<Cupon> agregarCupon(Cupon cupon) {
        cupones.add(cupon);
        return cupones;
    }

    public List<Cupon> EliminarCupon(Cupon cupon) {
        cupones.remove(cupon);
        return cupones;
    }

    public static Administrador getInstancia() {
        return instancia;
    }

    public Evento crearEvento(Evento evento) {
        return evento;
    }

    public List<Evento> eliminarEvento(Evento evento) {
        eventos.remove(evento);
        return eventos;
    }
}
