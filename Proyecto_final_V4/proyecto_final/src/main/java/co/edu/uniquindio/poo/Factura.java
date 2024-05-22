package co.edu.uniquindio.poo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Factura implements Serializable {
    private String codigo;
    private double subtotal;
    private double total;
    private LocalDate fecha;

    public Factura(double subtotal, double total) {
        this.codigo = UUID.randomUUID().toString();
        this.subtotal = subtotal;
        this.total = total;
        this.fecha = LocalDate.now();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

}