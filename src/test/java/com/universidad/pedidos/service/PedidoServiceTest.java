package com.universidad.pedidos.service;

import com.universidad.pedidos.domain.*;
import com.universidad.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock private PedidoRepository repo;
    @Mock private Notificador      notificacion;

    private PedidoService service;
    private DatosCliente  clienteValido;

    @BeforeEach
    void setUp() {
        service = new PedidoService(repo, notificacion);
        Direccion dir = new Direccion("Calle 5 #10-20", "Bogota", "110001");
        clienteValido = new DatosCliente("Juan Perez", "juan@example.com", "3001234567", dir);

        when(repo.save(any())).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(99L);
            return p;
        });
    }

    @Test
    void procesarPedido_sinDescuento_retornaOkConId() {
        LineaPedido[] lineas = { new LineaPedido(1L, 2, 100.0) };

        String resultado = service.procesarPedido(1L, clienteValido, lineas, "TARJETA", false, null);

        assertThat(resultado).isEqualTo("OK_99");
        verify(notificacion).notificarPedido(clienteValido, false);
    }

    @Test
    void procesarPedido_sinDescuento_calculaTotalCorrecto() {
        LineaPedido[] lineas = { new LineaPedido(1L, 2, 100.0) };

        service.procesarPedido(1L, clienteValido, lineas, "TARJETA", false, null);

        verify(repo).save(argThat(p -> cerca(p.getTotal(), 200.0)));
    }

    @Test
    void procesarPedido_conDescuentoVIP10_aplicaDescuento() {
        LineaPedido[] lineas = { new LineaPedido(1L, 1, 200.0) };
        CodigoDescuento descuento = new CodigoDescuento("VIP10");

        service.procesarPedido(1L, clienteValido, lineas, "EFECTIVO", false, descuento);

        verify(repo).save(argThat(p -> cerca(p.getTotal(), 180.0)));
    }

    @Test
    void procesarPedido_conDescuentoNEW20_aplicaDescuento() {
        LineaPedido[] lineas = { new LineaPedido(1L, 1, 100.0) };
        CodigoDescuento descuento = new CodigoDescuento("NEW20");

        service.procesarPedido(1L, clienteValido, lineas, "EFECTIVO", false, descuento);

        verify(repo).save(argThat(p -> cerca(p.getTotal(), 80.0)));
    }

    @Test
    void procesarPedido_urgente_aplicaRecargoDel15Porciento() {
        LineaPedido[] lineas = { new LineaPedido(1L, 1, 100.0) };

        service.procesarPedido(1L, clienteValido, lineas, "TARJETA", true, null);

        verify(repo).save(argThat(p -> cerca(p.getTotal(), 115.0)));
        verify(notificacion).notificarPedido(clienteValido, true);
    }

    @Test
    void procesarPedido_variosProductos_sumaCorrectamente() {
        LineaPedido[] lineas = {
            new LineaPedido(1L, 2, 50.0),  // 100
            new LineaPedido(2L, 3, 30.0)   //  90 → total 190
        };

        service.procesarPedido(1L, clienteValido, lineas, "TARJETA", false, null);

        verify(repo).save(argThat(p -> cerca(p.getTotal(), 190.0)));
    }

    @Test
    void procesarPedido_urgenteConDescuento_aplicaAmbos() {
        LineaPedido[] lineas = { new LineaPedido(1L, 1, 100.0) };
        CodigoDescuento descuento = new CodigoDescuento("VIP10");

        // total=100 → -10% → 90 → +15% urgente → 103.5
        service.procesarPedido(1L, clienteValido, lineas, "TARJETA", true, descuento);

        verify(repo).save(argThat(p -> Math.abs(p.getTotal() - 103.5) < 0.001));
    }

    @Test
    void procesarPedido_llamaNotificacionUnaVez() {
        LineaPedido[] lineas = { new LineaPedido(1L, 1, 50.0) };

        service.procesarPedido(1L, clienteValido, lineas, "TARJETA", false, null);

        verify(notificacion, times(1)).notificarPedido(any(), anyBoolean());
    }

    private static boolean cerca(double actual, double esperado) {
        return Math.abs(actual - esperado) < 0.001;
    }
}
