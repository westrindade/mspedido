package br.com.fiap.mspedidos.domain.controllers;

import br.com.fiap.mspedidos.domain.dto.PedidoDtoRequestConsumer;
import br.com.fiap.mspedidos.domain.service.PedidoService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class PedidoConsumer {

    private final PedidoService pedidoService;

    public PedidoConsumer(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Bean(name="confirmar-entrega/{id}")
    Consumer<PedidoDtoRequestConsumer> consumer(){
        return pedidoDtoRequestConsumer -> System.out.println("Chegou o " + pedidoDtoRequestConsumer.idPedido());
    }
}
