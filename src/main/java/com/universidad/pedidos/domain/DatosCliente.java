package com.universidad.pedidos.domain;

import java.util.Objects;

public final class DatosCliente {

    private final String nombre;
    private final String email;
    private final String telefono;
    private final Direccion direccion;

    public DatosCliente(String nombre, String email, String telefono, Direccion direccion) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Nombre requerido");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Email invalido");
        this.nombre    = nombre;
        this.email     = email;
        this.telefono  = telefono;
        this.direccion = direccion;
    }

    public String    getNombre()   { return nombre; }
    public String    getEmail()    { return email; }
    public String    getTelefono() { return telefono; }
    public Direccion getDireccion(){ return direccion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatosCliente c)) return false;
        return Objects.equals(nombre, c.nombre)
            && Objects.equals(email, c.email)
            && Objects.equals(telefono, c.telefono)
            && Objects.equals(direccion, c.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, email, telefono, direccion);
    }
}
