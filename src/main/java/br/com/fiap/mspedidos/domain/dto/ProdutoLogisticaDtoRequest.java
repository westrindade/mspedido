package br.com.fiap.mspedidos.domain.dto;

public record ProdutoLogisticaDtoRequest(
        Long codigoProduto,
        Long quantidade
) {
}
