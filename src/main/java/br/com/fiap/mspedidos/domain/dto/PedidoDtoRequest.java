package br.com.fiap.mspedidos.domain.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.mspedidos.domain.entities.FormaPagamentoEnum;
import br.com.fiap.mspedidos.domain.entities.ItemEntity;
import br.com.fiap.estrutura.exception.BusinessException;

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
            try {
                itemEntityList.add(new ItemEntity(item.idProduto(),item.quantidade()));
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        });
        return itemEntityList;
    }
}
