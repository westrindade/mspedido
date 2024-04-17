package br.com.fiap.mspedidos.domain.repositories;

import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.entities.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByIdCliente(Long idCliente);
    List<PedidoEntity> findByStatusPedido(StatusPedidoEnum statusPedido);
}
