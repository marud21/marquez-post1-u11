package com.universidad.pedidos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ValueObjectsTest {

    private final Direccion direccionValida = new Direccion("Calle 1 #2-3", "Bogota", "110001");

    // --- Direccion ---

    @Test
    void direccion_rechazaCiudadNula() {
        assertThatThrownBy(() -> new Direccion("Calle 1", null, "110001"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ciudad requerida");
    }

    @Test
    void direccion_igualdadPorValor() {
        Direccion a = new Direccion("Calle 1", "Bogota", "110001");
        Direccion b = new Direccion("Calle 1", "Bogota", "110001");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    // --- DatosCliente ---

    @Test
    void datosCliente_rechazaNombreBlanco() {
        assertThatThrownBy(() -> new DatosCliente("  ", "a@b.com", "123", direccionValida))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nombre requerido");
    }

    @Test
    void datosCliente_rechazaEmailSinArroba() {
        assertThatThrownBy(() -> new DatosCliente("Juan", "invalido", "123", direccionValida))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email invalido");
    }

    @Test
    void datosCliente_esInmutable() {
        DatosCliente cliente = new DatosCliente("Ana", "ana@x.com", "300", direccionValida);
        assertThat(cliente.getNombre()).isEqualTo("Ana");
        assertThat(cliente.getEmail()).isEqualTo("ana@x.com");
        assertThat(cliente.getDireccion()).isEqualTo(direccionValida);
    }

    @Test
    void datosCliente_igualdadPorValor() {
        DatosCliente a = new DatosCliente("Ana", "ana@x.com", "300", direccionValida);
        DatosCliente b = new DatosCliente("Ana", "ana@x.com", "300", direccionValida);
        assertThat(a).isEqualTo(b);
    }

    // --- LineaPedido ---

    @Test
    void lineaPedido_rechazaCantidadCero() {
        assertThatThrownBy(() -> new LineaPedido(1L, 0, 50.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cantidad debe ser positiva");
    }

    @Test
    void lineaPedido_rechazaPrecioNegativo() {
        assertThatThrownBy(() -> new LineaPedido(1L, 1, -1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Precio no puede ser negativo");
    }

    @Test
    void lineaPedido_rechazaProductoIdNulo() {
        assertThatThrownBy(() -> new LineaPedido(null, 1, 10.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ProductoId requerido");
    }

    // --- CodigoDescuento ---

    @Test
    void codigoDescuento_VIP10_retornaDiezPorciento() {
        CodigoDescuento d = new CodigoDescuento("VIP10");
        assertThat(d.getPorcentaje()).isEqualTo(0.10);
    }

    @Test
    void codigoDescuento_NEW20_retornaVeintePorciento() {
        CodigoDescuento d = new CodigoDescuento("NEW20");
        assertThat(d.getPorcentaje()).isEqualTo(0.20);
    }

    @Test
    void codigoDescuento_SALE30_retornaTreintaPorciento() {
        CodigoDescuento d = new CodigoDescuento("SALE30");
        assertThat(d.getPorcentaje()).isEqualTo(0.30);
    }

    @Test
    void codigoDescuento_rechazaCodigoDesconocido() {
        assertThatThrownBy(() -> new CodigoDescuento("FAKE99"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("FAKE99");
    }

    @Test
    void codigoDescuento_rechazaCodigoVacio() {
        assertThatThrownBy(() -> new CodigoDescuento("  "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void codigoDescuento_igualdadPorValor() {
        assertThat(new CodigoDescuento("VIP10")).isEqualTo(new CodigoDescuento("VIP10"));
    }
}
