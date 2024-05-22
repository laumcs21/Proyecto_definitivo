package co.edu.uniquindio.poo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Evento implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String ciudad;
    private String descripcion;
    private TipoEvento tipoEvento;
    private String imagen;
    private LocalDate fecha;
    private String direccion;
    private List<Localidad> localidades;

    public Evento(String nombre, String ciudad, String descripcion, TipoEvento tipoEvento, String imagen,
                  LocalDate fecha, String direccion, List<Localidad> localidades) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.tipoEvento = tipoEvento;
        this.imagen = imagen;
        this.fecha = fecha;
        this.direccion = direccion;
        this.localidades = localidades;
    }

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(List<Localidad> localidades) {
        this.localidades = localidades;
    }

    // Calcula el costo total de comprar boletos para un evento, aplicando un descuento si se proporciona un cupón válido
    public double calcularTotal(Localidad localidad, int numLocalidades, String cuponCodigo, List<Cupon> cupones) {
        double precioLocalidad = localidad.getPrecio();
        double total = precioLocalidad * numLocalidades;

        if (cuponCodigo != null && !cuponCodigo.isEmpty()) {
            for (Cupon cupon : cupones) {
                if (cupon.getNombreCupon().equals(cuponCodigo) && !cupon.isUsado()) {
                    total -= (total * cupon.getPorcentajeDescuento() / 100);
                    cupon.setUsado(true);
                    break;
                }
            }
        }
        return total;
    }

    @Override
    public Evento clone() {
        try {
            Evento cloned = (Evento) super.clone();
            // Clonando la lista de localidades para asegurar que el clon tenga una copia independiente de la lista
            cloned.localidades = localidades != null ? new ArrayList<>(localidades) : null;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("El objeto Evento no se puede clonar", e);
        }
    }
}