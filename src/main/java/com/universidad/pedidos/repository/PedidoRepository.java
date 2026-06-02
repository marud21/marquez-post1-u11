package com.universidad.pedidos.repository;

import com.universidad.pedidos.domain.Pedido;
import com.universidad.pedidos.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Producto findProductoById(Long id);
}
