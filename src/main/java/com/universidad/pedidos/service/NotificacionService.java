package com.universidad.pedidos.service;

import com.universidad.pedidos.domain.DatosCliente;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService implements Notificador {

    public void notificarPedido(DatosCliente cliente, boolean urgente) {
        System.out.println("Enviando email a: " + cliente.getEmail());
        System.out.println("Pedido urgente: " + urgente);
        System.out.println("Ciudad de entrega: " + cliente.getDireccion().getCiudad());
    }
}
