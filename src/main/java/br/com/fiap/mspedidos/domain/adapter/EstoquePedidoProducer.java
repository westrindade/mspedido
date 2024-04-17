package br.com.fiap.mspedidos.domain.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "estoque", url = "${estoque.url}")
public interface EstoquePedidoProducer {
    @PutMapping(value = "/{id}/estoque/diminuir/{quantidade}")
    void removerEstoque(@PathVariable Long id, @PathVariable Long quantidade);

    @PutMapping(value = "/{id}/estoque/acrescentar/{quantidade}")
    void devolverAoEstoque(@PathVariable Long id, @PathVariable Long quantidade);
}
