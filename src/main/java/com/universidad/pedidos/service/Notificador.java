package com.universidad.pedidos.service;

import com.universidad.pedidos.domain.DatosCliente;

public interface Notificador {
    void notificarPedido(DatosCliente cliente, boolean urgente);
}
