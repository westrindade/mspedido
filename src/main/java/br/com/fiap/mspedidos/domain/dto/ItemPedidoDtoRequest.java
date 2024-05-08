package br.com.fiap.mspedidos.domain.dto;

public record ItemPedidoDtoRequest(
        Long idProduto,
        Long quantidade
) { }
