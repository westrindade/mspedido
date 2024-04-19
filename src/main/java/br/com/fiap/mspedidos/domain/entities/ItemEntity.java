package br.com.fiap.mspedidos.domain.entities;

import br.com.fiap.mspedidos.domain.exceptions.BusinessException;
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

    public ItemEntity(Long idProduto, Long quantidade) throws BusinessException {
        if (idProduto == 0){
            throw new BusinessException("Produto não informado");
        }
        if (quantidade == 0){
            throw new BusinessException("Quantidade deve ser maior que zero");
        }
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.valorTotal = BigDecimal.ZERO;
    }

    public void informarPedido(PedidoEntity pedido) throws BusinessException {
        if (pedido == null){
            throw new BusinessException("Pedido deve ser informado");
        }
        this.pedido = pedido;
    }

    public void setValorTotal(BigDecimal valorTotal) throws BusinessException {
        if (valorTotal.compareTo(BigDecimal.ZERO) == 0){
            throw new BusinessException("Valor informado não pode ser zero");
        }
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
