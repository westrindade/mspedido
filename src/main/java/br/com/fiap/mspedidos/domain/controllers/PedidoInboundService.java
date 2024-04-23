package br.com.fiap.mspedidos.domain.controllers;

import org.springframework.integration.annotation.ServiceActivator;
import br.com.fiap.mspedidos.domain.exceptions.BusinessException;
import br.com.fiap.mspedidos.domain.service.PedidoService;
import org.springframework.stereotype.Service;

@Service
public class PedidoInboundService {

    private final PedidoService pedidoService;

    public PedidoInboundService(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @ServiceActivator(inputChannel = "confirmarEntrega")
    public void entregarPedido(Long id) throws BusinessException {
        pedidoService.entregarPedido(id);
    }

}
