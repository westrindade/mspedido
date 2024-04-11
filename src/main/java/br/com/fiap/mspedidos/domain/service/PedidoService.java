package br.com.fiap.mspedidos.domain.service;

import br.com.fiap.mspedidos.domain.dto.PedidoDtoRequest;
import br.com.fiap.mspedidos.domain.dto.PedidoDtoResponse;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.entities.PedidoEntity;
import br.com.fiap.mspedidos.domain.expections.BusinessException;
import br.com.fiap.mspedidos.domain.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<PedidoDtoResponse> listarPedidosPorCliente(Long idCliente) throws BusinessException {
        return pedidoRepository.findByIdCliente(idCliente).stream().map(PedidoEntity::toDto).toList();
    }

    public List<PedidoDtoResponse> listarPedidosPorStatus(StatusPedidoEnum statusPedido) throws BusinessException {
        return pedidoRepository.findByStatusPedido(statusPedido).stream().map(PedidoEntity::toDto).toList();
    }

    public PedidoDtoResponse buscarPedidos(Long id) throws BusinessException {
        PedidoEntity pedido = pedidoRepository.findById(id).orElse(null);
        if(pedido == null){
            throw new BusinessException("Pedido não encontrado");
        }
        return pedido.toDto();
    }

    public PedidoDtoResponse criar(PedidoDtoRequest pedidoDtoRequest) throws BusinessException {
        PedidoEntity pedido = new PedidoEntity(
                pedidoDtoRequest.idCliente(),
                pedidoDtoRequest.formaPagamento(),
                pedidoDtoRequest.quantidadeParcelas(),
                pedidoDtoRequest.itens()
        );
        return pedidoRepository.save(pedido).toDto();
    }

    public void confirmarPagamento(Long id) throws BusinessException {
        buscarPedidos(id);
        PedidoEntity pedido = new PedidoEntity(id);
        pedido.confirmarPagamento();
        pedidoRepository.save(pedido);
    }

    public void alterarStatusPedidoParaAguardarEnvio(Long id) throws BusinessException {
        buscarPedidos(id);
        PedidoEntity pedido = new PedidoEntity(id);
        pedido.aguardarEnvioPedido();
        pedidoRepository.save(pedido);
    }

    public void enviarPedito(Long id) throws BusinessException {
        buscarPedidos(id);
        PedidoEntity pedido = new PedidoEntity(id);
        pedido.enviarPedido();
        pedidoRepository.save(pedido);
    }

    public void cancelarPedito(Long id) throws BusinessException {
        buscarPedidos(id);
        PedidoEntity pedido = new PedidoEntity(id);
        pedido.cancelarPedido();
        pedidoRepository.save(pedido);
    }
}
