package com.universidad.pedidos.service;

import com.universidad.pedidos.domain.Pedido;
import com.universidad.pedidos.domain.Producto;
import com.universidad.pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Code Smell: Large Class — mezcla validacion, calculo, notificacion y persistencia
@Service
public class PedidoService {

    @Autowired // Code Smell: inyeccion en campo (deberia ser constructor)
    private PedidoRepository repo;

    // Code Smell: Long Method — valida, calcula, aplica descuento, notifica y persiste
    // Code Smell: Primitive Obsession — 12 parametros primitivos en lugar de Value Objects
    public String procesarPedido(Long clienteId, String clienteNombre,
                                 String clienteEmail, String clienteTelefono,
                                 String clienteDireccion, String clienteCiudad,
                                 String clienteCodigoPostal, List<Long> productosIds,
                                 List<Integer> cantidades, String metodoPago,
                                 boolean esUrgente, String codigoDescuento) {

        // Validacion del cliente (deberia ser metodo separado)
        if (clienteId == null || clienteNombre == null
                || clienteNombre.isBlank() || clienteEmail == null
                || !clienteEmail.contains("@")) {
            return "ERROR_CLIENTE";
        }

        // Validacion de listas (deberia ser metodo separado)
        if (productosIds == null || cantidades == null
                || productosIds.isEmpty() || productosIds.size() != cantidades.size()) {
            return "ERROR_LINEAS";
        }

        // Calculo de total (Long Method smell — mezcla iteracion con logica de negocio)
        double total = 0;
        for (int i = 0; i < productosIds.size(); i++) {
            Producto p = repo.findProductoById(productosIds.get(i));
            if (p == null) return "ERROR_PRODUCTO";
            if (cantidades.get(i) <= 0) return "ERROR_CANTIDAD";
            total += p.getPrecio() * cantidades.get(i);
        }

        // Descuento (logica de negocio mezclada con el flujo principal)
        if (codigoDescuento != null && codigoDescuento.equals("VIP10")) {
            total = total * 0.90;
        } else if (codigoDescuento != null && codigoDescuento.equals("NEW20")) {
            total = total * 0.80;
        } else if (codigoDescuento != null && codigoDescuento.equals("SALE30")) {
            total = total * 0.70;
        } else if (codigoDescuento != null && !codigoDescuento.isBlank()) {
            return "ERROR_DESCUENTO";
        }

        // Recargo urgente (responsabilidad mezclada)
        if (esUrgente) {
            total = total * 1.15;
        }

        // Notificacion (responsabilidad ajena — deberia ser Extract Class)
        System.out.println("Enviando email a: " + clienteEmail);
        System.out.println("Pedido urgente: " + esUrgente);
        System.out.println("Ciudad de entrega: " + clienteCiudad);
        System.out.println("Metodo de pago: " + metodoPago);

        // Persistencia
        Pedido pedido = new Pedido(clienteId, clienteNombre, total);
        return "OK_" + repo.save(pedido).getId();
    }
}
