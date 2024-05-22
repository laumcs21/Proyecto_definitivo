package co.edu.uniquindio.poo;

import java.io.Serializable;
import java.time.LocalDate;

public class Cupon implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombreCupon;
    private String codigo;
    private LocalDate fecha;
    private double porcentajeDescuento;
    private boolean usado;

    public Cupon(String nombreCupon, String codigo, LocalDate fecha, double porcentajeDescuento) {
        assert nombreCupon != null;
        assert fecha != null;
        assert porcentajeDescuento >= 0 && porcentajeDescuento <= 100;

        this.nombreCupon = nombreCupon;
        this.codigo = codigo;
        this.fecha = fecha;
        this.porcentajeDescuento = porcentajeDescuento;
        this.usado = false;
    }

    public String getNombreCupon() {
        return nombreCupon;
    }

    public void setNombreCupon(String nombreCupon) {
        this.nombreCupon = nombreCupon;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
