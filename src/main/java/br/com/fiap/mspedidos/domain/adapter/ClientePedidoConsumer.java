package br.com.fiap.mspedidos.domain.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.fiap.mspedidos.domain.dto.ClienteDtoResponse;

@FeignClient(name = "cliente", url = "${cliente.url}")
public interface ClientePedidoConsumer {
    @GetMapping(value = "/{id}")
    ClienteDtoResponse obterCliente(@PathVariable Long id);
}
