package com.universidad.pedidos.service;

import com.universidad.pedidos.domain.CodigoDescuento;
import com.universidad.pedidos.domain.DatosCliente;
import com.universidad.pedidos.domain.LineaPedido;
import com.universidad.pedidos.domain.Pedido;
import com.universidad.pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PedidoService {

    private final PedidoRepository repo;
    private final Notificador      notificacion;

    public PedidoService(PedidoRepository repo, Notificador notificacion) {
        this.repo         = repo;
        this.notificacion = notificacion;
    }

    public String procesarPedido(Long clienteId, DatosCliente cliente,
                                 LineaPedido[] lineas, String metodoPago,
                                 boolean esUrgente, CodigoDescuento descuento) {
        double total = calcularTotal(lineas);
        total = aplicarDescuento(total, descuento);
        total = aplicarRecargoUrgente(total, esUrgente);
        notificacion.notificarPedido(cliente, esUrgente);
        return persistirPedido(clienteId, cliente, total);
    }

    private double calcularTotal(LineaPedido[] lineas) {
        return Arrays.stream(lineas)
                .mapToDouble(l -> l.getPrecioUnitario() * l.getCantidad())
                .sum();
    }

    private double aplicarDescuento(double total, CodigoDescuento descuento) {
        return descuento != null ? total * (1 - descuento.getPorcentaje()) : total;
    }

    private double aplicarRecargoUrgente(double total, boolean esUrgente) {
        return esUrgente ? total * 1.15 : total;
    }

    private String persistirPedido(Long clienteId, DatosCliente cliente, double total) {
        Pedido pedido = new Pedido(clienteId, cliente.getNombre(), total);
        return "OK_" + repo.save(pedido).getId();
    }
}
