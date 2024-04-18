package br.com.fiap.mspedidos.domain.service;

import br.com.fiap.mspedidos.domain.adapter.EstoquePedidoProducer;
import br.com.fiap.mspedidos.domain.adapter.ProdutoPedidoProducer;
import br.com.fiap.mspedidos.domain.dto.PedidoDtoRequest;
import br.com.fiap.mspedidos.domain.dto.PedidoDtoResponse;
import br.com.fiap.mspedidos.domain.dto.ProdutoDtoResponse;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.entities.PedidoEntity;
import br.com.fiap.mspedidos.domain.excections.BusinessException;
import br.com.fiap.mspedidos.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    private final EstoquePedidoProducer estoquePedidoProducer;
    private final ProdutoPedidoProducer produtoPedidoProducer;

    public PedidoService(EstoquePedidoProducer estoquePedidoProducer, ProdutoPedidoProducer produtoPedidoProducer){
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

    public void confirmarPagamento(Long id) throws BusinessException {
        PedidoEntity pedido = buscarPedidoEntity(id);
        pedido.confirmarPagamento();
        pedidoRepository.save(pedido);
    }

    public void alterarStatusPedidoParaAguardarEnvio(Long id) throws BusinessException {
        PedidoEntity pedido = buscarPedidoEntity(id);
        pedido.aguardarEnvioPedido();
        pedidoRepository.save(pedido);
    }

    public void enviarPedido(Long id) throws BusinessException {
        PedidoEntity pedido = buscarPedidoEntity(id);
        pedido.enviarPedido();
        pedidoRepository.save(pedido);
    }

    public void cancelarPedido(Long id) throws BusinessException {
        PedidoEntity pedido = buscarPedidoEntity(id);
        this.devolverAoEstoqueProduto(pedido);
        pedido.cancelarPedido();
        pedidoRepository.save(pedido);
    }
    private void calcularValorPedido(PedidoEntity pedido){
        pedido.getItens().forEach(item -> {
            ProdutoDtoResponse produtoDtoResponse = this.produtoPedidoProducer.obterProduto(item.getIdProduto());
            item.setValorTotal(this.calcularValorItem(item.getQuantidade(),produtoDtoResponse.preco()));
            pedido.somarValorTotal(item.getValorTotal());
        });
    }
    private BigDecimal calcularValorItem(Long quantidade, double valorUnitario){
        return BigDecimal.valueOf( quantidade * valorUnitario);
    }
    private void removerEstoqueProduto(PedidoEntity pedido){
        pedido.getItens().forEach(item -> {
            this.estoquePedidoProducer.removerEstoque(item.getIdProduto(),item.getQuantidade());
        });
    }
    private void devolverAoEstoqueProduto(PedidoEntity pedido){
        pedido.getItens().forEach(item -> {
            this.estoquePedidoProducer.devolverAoEstoque(item.getIdProduto(),item.getQuantidade());
        });
    }
}
