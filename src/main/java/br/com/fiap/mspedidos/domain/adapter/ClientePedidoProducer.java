package br.com.fiap.mspedidos.domain.adapter;

import br.com.fiap.mspedidos.domain.dto.ClienteDtoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "cliente", url = "${cliente.url}")
public interface ClientePedidoProducer {
    @GetMapping(value = "/{id}")
    ClienteDtoResponse obterCliente(@PathVariable Long id);
}
