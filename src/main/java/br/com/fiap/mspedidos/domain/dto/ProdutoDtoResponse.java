package br.com.fiap.mspedidos.domain.dto;

public record ProdutoDtoResponse(
        int id,
        String nome,
        String descricao,
        int quantidadeEstoque,
        double preco
) {
}
