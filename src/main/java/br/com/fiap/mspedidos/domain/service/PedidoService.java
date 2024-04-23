package br.com.fiap.mspedidos.domain.service;

import br.com.fiap.mspedidos.domain.adapter.EstoquePedidoProducer;
import br.com.fiap.mspedidos.domain.adapter.ProdutoPedidoProducer;
import br.com.fiap.mspedidos.domain.dto.PedidoDtoRequest;
import br.com.fiap.mspedidos.domain.dto.PedidoDtoResponse;
import br.com.fiap.mspedidos.domain.dto.ProdutoDtoResponse;
import br.com.fiap.mspedidos.domain.entities.ItemEntity;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.entities.PedidoEntity;
import br.com.fiap.mspedidos.domain.exceptions.BusinessException;
import br.com.fiap.mspedidos.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private final PedidoRepository pedidoRepository;

    private final EstoquePedidoProducer estoquePedidoProducer;
    private final ProdutoPedidoProducer produtoPedidoProducer;

    public PedidoService(PedidoRepository pedidoRepository, EstoquePedidoProducer estoquePedidoProducer, ProdutoPedidoProducer produtoPedidoProducer){
        this.pedidoRepository = pedidoRepository;
        this.estoquePedidoProducer = estoquePedidoProducer;
        this.produtoPedidoProducer = produtoPedidoProducer;
    }

    private PedidoEntity buscarPedidoEntity(Long id) throws BusinessException {
        PedidoEntity pedido = pedidoRepository.findById(id).orElse(null);
        if(pedido == null){
            throw new BusinessException("Pedido não encontrado");
        }
        return pedido;
    }
    public List<PedidoDtoResponse> listarPedidosPorCliente(Long idCliente) throws BusinessException {
        return pedidoRepository.findByIdCliente(idCliente).stream().map(PedidoEntity::toDto).toList();
    }

    public List<PedidoDtoResponse> listarPedidosPorStatus(StatusPedidoEnum statusPedido) throws BusinessException {
        return pedidoRepository.findByStatusPedido(statusPedido).stream().map(PedidoEntity::toDto).toList();
    }

    public PedidoDtoResponse buscarPedidoPorId(Long id) throws BusinessException {
        return buscarPedidoEntity(id).toDto();
    }

    public PedidoDtoResponse criar(PedidoDtoRequest pedidoDtoRequest) throws BusinessException {
        final PedidoEntity pedido = new PedidoEntity(
                pedidoDtoRequest.idCliente(),
                pedidoDtoRequest.formaPagamento(),
                pedidoDtoRequest.quantidadeParcelas(),
                pedidoDtoRequest.ToEntityListItem(),
                pedidoDtoRequest.enderecoPedido().toEntity()
        );
        this.calcularValorPedido(pedido);
        this.removerEstoqueProduto(pedido);

        PedidoEntity pedidoRetorno = pedidoRepository.save(pedido);
        return pedidoRetorno.toDto();
    }

    public void pagar(Long id) throws BusinessException {
        PedidoEntity pedido = buscarPedidoEntity(id);
        pedido.pagarPedido();
        pedidoRepository.save(pedido);
    }

    public void alterarStatusPedidoParaAguardarEntrega(Long id) throws BusinessException {
        PedidoEntity pedido = buscarPedidoEntity(id);
        pedido.aguardarEntrega();
        pedidoRepository.save(pedido);
    }

    public void entregarPedido(Long id) throws BusinessException {
        PedidoEntity pedido = buscarPedidoEntity(id);
        pedido.entregarPedido();
        pedidoRepository.save(pedido);
    }

    public void cancelarPedido(Long id) throws BusinessException {
        PedidoEntity pedido = buscarPedidoEntity(id);
        pedido.cancelarPedido();
        this.devolverAoEstoqueProduto(pedido);
        pedidoRepository.save(pedido);
    }
    private void calcularValorPedido(PedidoEntity pedido) throws BusinessException {
        for (ItemEntity item : pedido.getItens()) {
            ProdutoDtoResponse produtoDtoResponse = this.produtoPedidoProducer.obterProduto(item.getIdProduto());
            if (produtoDtoResponse == null) {
                throw new BusinessException("Produto " + item.getIdProduto() + " não encontrado");
            }
            if (produtoDtoResponse.preco() == 0) {
                throw new BusinessException("Produto " + item.getIdProduto() + " não possue valor cadastrao");
            }
            item.setValorTotal(this.calcularValorItem(item.getQuantidade(),produtoDtoResponse.preco()));
            pedido.somarValorTotal(item.getValorTotal());
        }
    }
    private BigDecimal calcularValorItem(Long quantidade, double valorUnitario) {
        return BigDecimal.valueOf( quantidade * valorUnitario);
    }
    private void removerEstoqueProduto(PedidoEntity pedido) {
        pedido.getItens().forEach(item -> {
            this.estoquePedidoProducer.removerEstoque(item.getIdProduto(),item.getQuantidade());
        });
    }
    private void devolverAoEstoqueProduto(PedidoEntity pedido) {
        pedido.getItens().forEach(item -> {
            this.estoquePedidoProducer.devolverAoEstoque(item.getIdProduto(),item.getQuantidade());
        });
    }
}
