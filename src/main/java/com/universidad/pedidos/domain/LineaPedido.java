package com.universidad.pedidos.domain;

import java.util.Objects;

public final class LineaPedido {

    private final Long   productoId;
    private final int    cantidad;
    private final double precioUnitario;

    public LineaPedido(Long productoId, int cantidad, double precioUnitario) {
        if (productoId == null)
            throw new IllegalArgumentException("ProductoId requerido");
        if (cantidad <= 0)
            throw new IllegalArgumentException("Cantidad debe ser positiva");
        if (precioUnitario < 0)
            throw new IllegalArgumentException("Precio no puede ser negativo");
        this.productoId     = productoId;
        this.cantidad       = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Long   getProductoId()     { return productoId; }
    public int    getCantidad()        { return cantidad; }
    public double getPrecioUnitario()  { return precioUnitario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineaPedido l)) return false;
        return cantidad == l.cantidad
            && Double.compare(precioUnitario, l.precioUnitario) == 0
            && Objects.equals(productoId, l.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productoId, cantidad, precioUnitario);
    }
}
