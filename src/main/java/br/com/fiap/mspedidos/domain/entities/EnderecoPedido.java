package br.com.fiap.mspedidos.domain.entities;

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
    public EnderecoPedido(String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String estado) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }
}
