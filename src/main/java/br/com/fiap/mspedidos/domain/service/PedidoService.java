package br.com.fiap.mspedidos.domain.service;

import br.com.fiap.mspedidos.domain.adapter.ClientePedidoConsumer;
import br.com.fiap.mspedidos.domain.adapter.EstoquePedidoProducer;
import br.com.fiap.mspedidos.domain.adapter.ProdutoPedidoConsumer;
import br.com.fiap.mspedidos.domain.dto.*;
import br.com.fiap.mspedidos.domain.entities.ItemEntity;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.entities.PedidoEntity;
import br.com.fiap.estrutura.exception.BusinessException;
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
    private final ProdutoPedidoConsumer produtoPedidoConsumer;
    private final ClientePedidoConsumer clientePedidoConsumer;

    public PedidoService(PedidoRepository pedidoRepository,
                         EstoquePedidoProducer estoquePedidoProducer,
                         ProdutoPedidoConsumer produtoPedidoConsumer,
                         ClientePedidoConsumer clientePedidoConsumer
    ){
        this.pedidoRepository = pedidoRepository;
        this.estoquePedidoProducer = estoquePedidoProducer;
        this.produtoPedidoConsumer = produtoPedidoConsumer;
        this.clientePedidoConsumer = clientePedidoConsumer;
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

    public boolean clientePossuiPedidos(long idCliente){
        return pedidoRepository.existsByIdCliente(idCliente);
    }

    public List<PedidoDtoResponse> listarPedidosPorStatus(StatusPedidoEnum statusPedido) throws BusinessException {
        return pedidoRepository.findByStatusPedido(statusPedido).stream().map(PedidoEntity::toDto).toList();
    }

    public List<PedidoLogisticaDtoRequest> listarPedidosPorStatusSchedule(StatusPedidoEnum statusPedido) throws BusinessException {
        return pedidoRepository.findByStatusPedido(statusPedido).stream().map(PedidoEntity::toDtoLogistica).toList();
    }

    public PedidoDtoResponse buscarPedidoPorId(Long id) throws BusinessException {
        return buscarPedidoEntity(id).toDto();
    }

    public PedidoDtoResponse criar(PedidoDtoRequest pedidoDtoRequest) throws BusinessException {
        this.validarClienteExiste(pedidoDtoRequest.idCliente());
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
    private void validarClienteExiste(Long id) throws BusinessException {
        try {
            ClienteDtoResponse clienteDtoResponse = this.clientePedidoConsumer.obterCliente(id);
            if (clienteDtoResponse == null){
                throw new BusinessException("Cliente " + id + " não encontrado");
            }
        } catch (Exception ex) {
            throw new BusinessException("Conexão com msCliente não estabelecida");
        }
    }
    private void calcularValorPedido(PedidoEntity pedido) throws BusinessException {
        for (ItemEntity item : pedido.getItens()) {
            ProdutoDtoResponse produtoDtoResponse = this.produtoPedidoConsumer.obterProduto(item.getIdProduto());
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
    private void removerEstoqueProduto(PedidoEntity pedido) throws BusinessException {
        try{
            pedido.getItens().forEach(item -> {
                this.estoquePedidoProducer.removerEstoque(item.getIdProduto(),item.getQuantidade());
            });
        } catch (Exception ex) {
            throw new BusinessException("Conexão com msProduto não estabelecida");
        }
    }
    private void devolverAoEstoqueProduto(PedidoEntity pedido) throws BusinessException {
        try{
            pedido.getItens().forEach(item -> {
                this.estoquePedidoProducer.devolverAoEstoque(item.getIdProduto(),item.getQuantidade());
            });
        } catch (Exception ex) {
            throw new BusinessException("Conexão com msProduto não estabelecida");
        }
    }
}
