package br.com.fiap.mspedidos.domain.entities;

import br.com.fiap.estrutura.exception.BusinessException;
import jakarta.persistence.*;

@Embeddable
public class EnderecoPedido {
    @Column(name="cd_cep")
    private String cep;
    @Column(name="ds_logradouro")
    private String logradouro;
    @Column(name="ds_numero")
    private String numero;
    @Column(name="ds_complemento")
    private String complemento;
    @Column(name="nm_bairro")
    private String bairro;
    @Column(name="nm_cidade")
    private String cidade;
    @Column(name="nm_estado")
    private String estado;

    public EnderecoPedido(){}
    public EnderecoPedido(String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String estado) throws BusinessException {

        if (cep.isEmpty()){
            throw new BusinessException("Cep não informado");
        }

        if (logradouro.isEmpty()){
            throw new BusinessException("Logradouro não informado");
        }

        if (bairro.isEmpty()){
            throw new BusinessException("Bairro não informado");
        }

        if (numero.isEmpty()){
            throw new BusinessException("Número não informado");
        }

        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }
}
