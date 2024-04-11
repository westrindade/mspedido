package br.com.fiap.mspedidos.domain.dto;

import br.com.fiap.mspedidos.domain.entities.ItemEntity;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoDtoResponse(
        Long id,
        Long idCliente,
        List<ItemEntity>itens,
        StatusPedidoEnum statusPedidoEnum,
        LocalDateTime dataCriacao
) {
}
