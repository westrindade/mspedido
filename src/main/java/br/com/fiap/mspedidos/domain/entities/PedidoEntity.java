package br.com.fiap.mspedidos.domain.entities;

import br.com.fiap.mspedidos.domain.dto.ItemPedidoDtoResponse;
import br.com.fiap.mspedidos.domain.dto.PedidoDtoResponse;
import br.com.fiap.mspedidos.domain.dto.PedidoLogisticaDtoRequest;
import br.com.fiap.mspedidos.domain.dto.ProdutoLogisticaDtoRequest;
import br.com.fiap.estrutura.exception.BusinessException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column(name = "ds_status_pedido")
    private StatusPedidoEnum statusPedido;
    @Column(name = "dt_pagamento")
    private LocalDateTime dataPagamento;
    @Column(name = "dt_entrega")
    private LocalDateTime dataEntrega;
    @Column(name = "dt_cancelamento")
    private LocalDateTime dataCancelamento;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemEntity> itens;
    @Embedded
    private EnderecoPedido endereco = new EnderecoPedido();

    public PedidoEntity() { }
    public PedidoEntity(Long id, StatusPedidoEnum status){
        this.id = id;
        this.statusPedido = status;
    }
    public PedidoEntity(Long idCliente, FormaPagamentoEnum formaPagamento, int quantidadeParcelas, List<ItemEntity> itens, EnderecoPedido endereco)
            throws BusinessException {

        if (idCliente == 0){
            throw new BusinessException("Cliente não informado");
        }

        if (endereco == null){
            throw new BusinessException("Endereço do pedido não informado");
        }

        if (itens == null || itens.size() == 0){
            throw new BusinessException("Itens do pedido não informado");
        }

        if (quantidadeParcelas == 0){
            throw new BusinessException("Quantidade de Parcelas do Pedido não informada");
        }

        this.idCliente = idCliente;
        this.formaPagamento = formaPagamento;
        this.quantidadeParcelas = quantidadeParcelas;
        this.itens = itens;
        this.endereco = endereco;

        this.dataCriacao = LocalDateTime.now();
        this.statusPedido = StatusPedidoEnum.AGUARDANDO_PAGAMENTO;
        this.valorTotal = BigDecimal.ZERO;
    }

    public void pagarPedido() {
        this.statusPedido = StatusPedidoEnum.PAGO;
        this.dataPagamento = LocalDateTime.now();
    }

    public void cancelarPedido() throws BusinessException {
        if (this.statusPedido == StatusPedidoEnum.CANCELADO) {
            throw new BusinessException("Pedido não pode ser cancelado");
        }
        this.statusPedido = StatusPedidoEnum.CANCELADO;
        this.dataCancelamento = LocalDateTime.now();
    }

    public void entregarPedido() throws BusinessException {
        if (this.statusPedido == StatusPedidoEnum.AGUARDANDO_ENTREGA) {
            this.statusPedido = StatusPedidoEnum.ENTREGUE;
            this.dataEntrega = LocalDateTime.now();
        } else {
            throw new BusinessException("Pedido não pode ser entregue");
        }
    }

    public void aguardarEntrega() throws BusinessException {
        if (this.statusPedido == StatusPedidoEnum.PAGO) {
            this.statusPedido = StatusPedidoEnum.AGUARDANDO_ENTREGA;
        } else {
            throw new BusinessException("Pedido não pode ser direcionado para aguardando entrega");
        }
    }

    public PedidoDtoResponse toDto() {
        return new PedidoDtoResponse(
                this.id,
                this.idCliente,
                this.statusPedido,
                this.dataCriacao,
                this.toListDto()
        );
    }

    private List<ItemPedidoDtoResponse> toListDto(){
        List<ItemPedidoDtoResponse> itens = new ArrayList<>();
        if (this.itens != null && this.itens.size() > 0) {
            this.itens.forEach(item -> {
                itens.add(new ItemPedidoDtoResponse(item.getIdProduto(), item.getQuantidade()));
            });
        }
        return itens;
    }

    public PedidoLogisticaDtoRequest toDtoLogistica() {
        return new PedidoLogisticaDtoRequest(
                this.id,
                this.idCliente,
                this.dataCriacao,
                this.statusPedido,
                this.endereco.getCep(),
                this.endereco.getNumero(),
                this.endereco.getComplemento(),
                this.toListDtoProdutoLogistica()
        );
    }

    private List<ProdutoLogisticaDtoRequest> toListDtoProdutoLogistica(){
        List<ProdutoLogisticaDtoRequest> produtos = new ArrayList<>();
        this.itens.forEach(item -> {
            produtos.add(new ProdutoLogisticaDtoRequest(item.getIdProduto(), item.getQuantidade()));
        });
        return produtos;
    }

    public Long getId() {
        return id;
    }

    public List<ItemEntity> getItens() {
        return itens;
    }

    public void somarValorTotal(BigDecimal valorTotal) throws BusinessException {
        if (valorTotal.compareTo(BigDecimal.ZERO) == 0){
            throw new BusinessException("Valor informado não pode ser zero");
        }
        this.valorTotal = this.valorTotal.add(valorTotal);
    }

    @PrePersist
    public void prePersist() throws BusinessException {
        for (ItemEntity item : itens) {
            item.informarPedido(this);
        }
    }
}
