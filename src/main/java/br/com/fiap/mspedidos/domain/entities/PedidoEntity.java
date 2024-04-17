package br.com.fiap.mspedidos.domain.entities;

import jakarta.validation.constraints.NotNull;

import br.com.fiap.mspedidos.domain.dto.PedidoDtoResponse;
import br.com.fiap.mspedidos.domain.expections.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "tb_pedidos")
@Entity
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cd_pedido", unique = true)
    private Long id;
    @Column(name="cd_cliente")
    private Long idCliente;
    @Column(name = "dt_criacao")
    private LocalDateTime dataCriacao;
    @Column(name="nu_valor_total")
    private BigDecimal valorTotal;
    @Column(name = "ds_forma_Pagamento")
    private FormaPagamentoEnum formaPagamento;
    @Column(name = "nu_quantidade_parcelas")
    private int quantidadeParcelas;
    @Column(name = "ds_status_pagamento")
    private StatusPagamentoEnum statusPagamento;
    @Column(name = "ds_status_pedido")
    private StatusPedidoEnum statusPedido;
    @Column(name = "dt_pagamento")
    private LocalDateTime dataPagamento;
    @Column(name = "dt_envio")
    private LocalDateTime dataEnvio;
    @Column(name = "dt_cancelamento")
    private LocalDateTime dataCancelamento;
    @NotNull @Size(min = 1)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ItemEntity> itens;
    @Embedded
    private EnderecoPedido endereco = new EnderecoPedido();

    public PedidoEntity() { }
    public PedidoEntity(Long id, StatusPedidoEnum status){
        this.id = id;
        this.statusPedido = status;
    }
        public PedidoEntity(Long idCliente, FormaPagamentoEnum formaPagamento, int quantidadeParcelas, List<ItemEntity> itens, EnderecoPedido endereco) {
        this.idCliente = idCliente;
        this.formaPagamento = formaPagamento;
        this.quantidadeParcelas = quantidadeParcelas;
        this.itens = itens;
        this.endereco = endereco;

        this.dataCriacao = LocalDateTime.now();
        this.statusPagamento = StatusPagamentoEnum.AGUARDANDO_PAGAMENTO;
        this.statusPedido = StatusPedidoEnum.PREPARANDO;
    }

    public void confirmarPagamento() throws BusinessException {
        if (this.statusPagamento != StatusPagamentoEnum.AGUARDANDO_PAGAMENTO){
            throw new BusinessException("Pagamento não pode ser confirmado");
        }
        this.statusPagamento = StatusPagamentoEnum.PAGAMENTO_CONFIRMADO;
        this.dataPagamento = LocalDateTime.now();
    }

    public void cancelarPedido() throws BusinessException {
        if (this.statusPedido == StatusPedidoEnum.CANCELADO) {
            throw new BusinessException("Pedido não pode ser cancelado");
        }
        this.statusPedido = StatusPedidoEnum.CANCELADO;
        this.dataCancelamento = LocalDateTime.now();
    }

    public void enviarPedido() throws BusinessException {
        if (this.statusPedido == StatusPedidoEnum.AGUARDANDO_ENVIO) {
            this.statusPedido = StatusPedidoEnum.ENVIADO;
            this.dataEnvio = LocalDateTime.now();
        } else {
            throw new BusinessException("Pedido não pode ser enviado");
        }
    }

    public void aguardarEnvioPedido() throws BusinessException {
        if (this.statusPedido == StatusPedidoEnum.PREPARANDO) {
            this.statusPedido = StatusPedidoEnum.AGUARDANDO_ENVIO;
        }
        throw new BusinessException("Pedido não pode ser direcionado para aguardando envio");
    }

    public PedidoDtoResponse toDto() {
        return new PedidoDtoResponse(
                this.id,
                this.idCliente,
                this.statusPedido,
                this.statusPagamento,
                this.dataCriacao
        );
    }
    public Long getId() {
        return id;
    }

    public List<ItemEntity> getItens() {
        return itens;
    }

    public void somarValorTotal(BigDecimal valorTotal) {
        if (this.valorTotal == null)
            this.valorTotal = new BigDecimal(0);
        this.valorTotal = this.valorTotal.add(valorTotal);
    }

    @PrePersist
    public void prePersist() {
        for (ItemEntity item : itens) {
            item.setPedido(this);
        }
    }
}
