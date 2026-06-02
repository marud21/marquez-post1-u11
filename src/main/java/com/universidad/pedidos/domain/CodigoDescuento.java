package com.universidad.pedidos.domain;

import java.util.Map;
import java.util.Objects;

public final class CodigoDescuento {

    private static final Map<String, Double> DESCUENTOS = Map.of(
        "VIP10",  0.10,
        "NEW20",  0.20,
        "SALE30", 0.30
    );

    private final String codigo;
    private final double porcentaje;

    public CodigoDescuento(String codigo) {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("Codigo de descuento no puede ser vacio");
        Double pct = DESCUENTOS.get(codigo);
        if (pct == null)
            throw new IllegalArgumentException("Codigo de descuento no reconocido: " + codigo);
        this.codigo     = codigo;
        this.porcentaje = pct;
    }

    public String getCodigo()     { return codigo; }
    public double getPorcentaje() { return porcentaje; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodigoDescuento c)) return false;
        return Objects.equals(codigo, c.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
