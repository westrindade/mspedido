package br.com.fiap.mspedidos.domain.dto;

import br.com.fiap.mspedidos.domain.entities.EnderecoPedido;
import br.com.fiap.estrutura.exception.BusinessException;

public record EnderecoDtoRequest(
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado
) {
    public EnderecoPedido toEntity() throws BusinessException {
        return new EnderecoPedido(cep, logradouro,numero,complemento,bairro,cidade,estado);
    }
}
