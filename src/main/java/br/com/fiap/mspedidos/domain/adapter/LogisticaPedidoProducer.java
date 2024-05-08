package br.com.fiap.mspedidos.domain.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.mspedidos.domain.dto.PedidoLogisticaDtoRequest;

@FeignClient(name = "logistica", url = "${logistica.url}")
public interface LogisticaPedidoProducer {
    @PutMapping(value = "/processar/pedido")
    void enviarLogistica(@RequestBody PedidoLogisticaDtoRequest pedidoLogisticaDtoRequest);
}
