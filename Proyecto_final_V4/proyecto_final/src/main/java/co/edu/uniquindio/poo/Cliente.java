package co.edu.uniquindio.poo;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nombre;
    private String cedula;
    private String telefono;
    private String usuario;
    private String contraseña;
    private String codigoVerificacion;
    private String cuponPrimeraVez;
    private boolean verificado;

    public Cliente(String nombre, String cedula, String telefono, String usuario, String contraseña) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.verificado = false;
        this.cuponPrimeraVez = "NUEVO15";
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public String getCuponPrimeraVez() {
        return cuponPrimeraVez;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setCuponPrimeraVez(String codigoCuponPrimeraVez) {
        this.cuponPrimeraVez = codigoCuponPrimeraVez;
    }
    
}
