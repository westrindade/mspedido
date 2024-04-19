package br.com.fiap.mspedidos.domain.dto;

import br.com.fiap.mspedidos.domain.entities.ItemEntity;
import br.com.fiap.mspedidos.domain.exceptions.BusinessException;

public record ItemPedidoDtoRequest(
        Long idProduto,
        Long quantidade
) { }
