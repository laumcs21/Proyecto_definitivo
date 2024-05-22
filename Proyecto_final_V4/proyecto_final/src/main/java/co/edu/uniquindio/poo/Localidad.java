package co.edu.uniquindio.poo;

import java.io.Serializable;

public class Localidad implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private double precio;
    private int capacidadMaxima;
    private Evento eventoAsociado;

    public Localidad(String nombre, double precio, int capacidadMaxima) {
        this.nombre = nombre;
        this.precio = precio;
        this.capacidadMaxima = capacidadMaxima;
    }

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Evento getEventoAsociado() {
        return eventoAsociado;
    }

    public void setEventoAsociado(Evento eventoAsociado) {
        this.eventoAsociado = eventoAsociado;
    }

    public boolean hayCapacidadDisponible(int cantidad) {
        return capacidadMaxima >= cantidad;
    }

    public boolean esPrecioValido() {
        return precio >= 0;
    }
   //Reduce la capacidad máxima de la entidad en una cantidad especificada
    public void reducirCapacidad(int cantidad) {
        this.capacidadMaxima -= cantidad;
    }

    @Override
    public String toString() {
        return "Localidad: " + nombre + ", Precio: $" + precio + ", Capacidad Máxima: " + capacidadMaxima;
    }
}
