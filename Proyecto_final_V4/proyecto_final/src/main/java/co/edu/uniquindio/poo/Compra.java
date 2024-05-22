package co.edu.uniquindio.poo;

import java.io.Serializable;
import java.time.LocalDate;

public class Compra implements Serializable {
    private static final long serialVersionUID = 1L;
    private Cliente cliente;
    private Evento evento;
    private Localidad localidad;
    private Cupon cupon;
    private int numeroLocalidades;
    private LocalDate fechaCompra;
    private double totalCompra;
    private Factura factura;

    public Compra(Cliente cliente, Evento evento, Localidad localidad, Cupon cupon, Factura factura) {
        this.cliente = cliente;
        this.evento = evento;
        this.localidad = localidad;
        this.cupon = cupon;
        this.factura = factura;
        this.fechaCompra = LocalDate.now();
    }

    private Compra(Builder builder) {
        this.cliente = builder.cliente;
        this.evento = builder.evento;
        this.localidad = builder.localidad;
        this.cupon = builder.cupon;
        this.numeroLocalidades = builder.numeroLocalidades;
        this.fechaCompra = builder.fechaCompra != null ? builder.fechaCompra : LocalDate.now();
        this.totalCompra = builder.totalCompra;
        this.factura = builder.factura;
    }

    public static class Builder {
        private Cliente cliente;
        private Evento evento;
        private Localidad localidad;
        private Cupon cupon;
        private int numeroLocalidades;
        private LocalDate fechaCompra;
        private double totalCompra;
        private Factura factura;

        public Builder(Cliente cliente, Evento evento, Localidad localidad) {
            this.cliente = cliente;
            this.evento = evento;
            this.localidad = localidad;
        }

        public Builder cupon(Cupon cupon) {
            this.cupon = cupon;
            return this;
        }

        public Builder numeroLocalidades(int numeroLocalidades) {
            this.numeroLocalidades = numeroLocalidades;
            return this;
        }

        public Builder fechaCompra(LocalDate fechaCompra) {
            this.fechaCompra = fechaCompra;
            return this;
        }

        public Builder totalCompra(double totalCompra) {
            this.totalCompra = totalCompra;
            return this;
        }

        public Builder factura(Factura factura) {
            this.factura = factura;
            return this;
        }

        public Compra build() {
            return new Compra(this);
        }
    }

    // Getters and setters
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public int getNumeroLocalidades() {
        return numeroLocalidades;
    }

    public void setNumeroLocalidades(int numeroLocalidades) {
        this.numeroLocalidades = numeroLocalidades;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }
}
