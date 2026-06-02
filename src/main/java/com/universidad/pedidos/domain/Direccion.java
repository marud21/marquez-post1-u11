package com.universidad.pedidos.domain;

import java.util.Objects;

public final class Direccion {

    private final String calle;
    private final String ciudad;
    private final String codigoPostal;

    public Direccion(String calle, String ciudad, String codigoPostal) {
        if (ciudad == null || ciudad.isBlank())
            throw new IllegalArgumentException("Ciudad requerida");
        this.calle = calle;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }

    public String getCalle()        { return calle; }
    public String getCiudad()       { return ciudad; }
    public String getCodigoPostal() { return codigoPostal; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Direccion d)) return false;
        return Objects.equals(calle, d.calle)
            && Objects.equals(ciudad, d.ciudad)
            && Objects.equals(codigoPostal, d.codigoPostal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calle, ciudad, codigoPostal);
    }

    @Override
    public String toString() {
        return calle + ", " + ciudad + " " + codigoPostal;
    }
}
