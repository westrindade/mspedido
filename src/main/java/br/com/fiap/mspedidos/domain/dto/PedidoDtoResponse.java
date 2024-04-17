package br.com.fiap.mspedidos.domain.dto;

import br.com.fiap.mspedidos.domain.entities.StatusPagamentoEnum;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;

import java.time.LocalDateTime;

public record PedidoDtoResponse(
        Long id,
        Long idCliente,
        StatusPedidoEnum statusPedido,
        StatusPagamentoEnum statusPagamento,
        LocalDateTime dataCriacao
) {
}
