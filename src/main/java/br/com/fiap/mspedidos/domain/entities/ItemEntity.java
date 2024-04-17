package br.com.fiap.mspedidos.domain.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Table(name = "tb_pedido_item")
@Entity
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cd_item_pedido", unique = true)
    private Long id;
    @Column(name="cd_produto")
    private Long idProduto;
    @Column(name="nu_quantidade")
    private Long quantidade;
    @Column(name="nu_valor_total")
    private BigDecimal valorTotal;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;

    public ItemEntity() {
    }

    public ItemEntity(Long idProduto, Long quantidade) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    public Long getIdProduto() {
        return idProduto;
    }
    public Long getQuantidade() {
        return quantidade;
    }
}
