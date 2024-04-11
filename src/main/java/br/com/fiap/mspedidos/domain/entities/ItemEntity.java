package br.com.fiap.mspedidos.domain.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

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
    private BigDecimal quantidade;
    @Column(name="nu_valor_total")
    private BigDecimal valorTotal;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;

    public ItemEntity(PedidoEntity pedido, Long idProduto, BigDecimal quantidade, BigDecimal valorTotal) {
        this.pedido = pedido;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }
}
