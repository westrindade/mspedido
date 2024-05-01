package br.com.fiap.mspedidos.domain.dto;

import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoLogisticaDtoRequest(
        Long codigoPedido,
        Long codigoCliente,
        LocalDateTime dataCriacao,
        StatusPedidoEnum statusPedido,
        String cep,
        String numeroEndereco,
        String complementoEndereco,
        List<ProdutoLogisticaDtoRequest> produtos
) {
}
