package br.com.fiap.mspedidos.domain.adapter;

import br.com.fiap.mspedidos.domain.dto.ProdutoDtoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produto", url = "${produto.url}")
public interface ProdutoPedidoConsumer {
    @GetMapping(value = "/{id}")
    ProdutoDtoResponse obterProduto(@PathVariable("id") Long id);
}
