package br.com.fiap.mspedidos.domain.dto;

import br.com.fiap.mspedidos.domain.entities.FormaPagamentoEnum;
import br.com.fiap.mspedidos.domain.entities.ItemEntity;

import java.util.List;
import java.util.UUID;

public record PedidoDtoRequest(
        Long idCliente,
        List<ItemEntity>itens,
        FormaPagamentoEnum formaPagamento,
        int quantidadeParcelas
) {
}
