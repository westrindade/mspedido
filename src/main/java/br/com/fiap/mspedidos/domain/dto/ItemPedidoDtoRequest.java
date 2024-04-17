package br.com.fiap.mspedidos.domain.dto;

import br.com.fiap.mspedidos.domain.entities.ItemEntity;
import br.com.fiap.mspedidos.domain.expections.BusinessException;

public record ItemPedidoDtoRequest(
        Long idProduto,
        Long quantidade
) {
    public ItemEntity toEntity() throws BusinessException {
        return new ItemEntity(this.idProduto,this.quantidade);
    }
}
