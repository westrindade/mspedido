package br.com.fiap.mspedidos.domain.dto;

import br.com.fiap.mspedidos.domain.entities.FormaPagamentoEnum;
import br.com.fiap.mspedidos.domain.entities.ItemEntity;
import br.com.fiap.mspedidos.domain.expections.BusinessException;

import java.util.ArrayList;
import java.util.List;

public record PedidoDtoRequest(
        Long idCliente,
        List<ItemPedidoDtoRequest> itens,
        FormaPagamentoEnum formaPagamento,
        int quantidadeParcelas,
        EnderecoDtoRequest enderecoPedido
) {
    public List<ItemEntity> ToEntityListItem() throws BusinessException {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        itens.forEach(item -> {
            itemEntityList.add(new ItemEntity(item.idProduto(),item.quantidade()));
        });
        return itemEntityList;
    }
}
