package br.com.fiap.mspedidos;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "logistica", url = "http://localhost:8081/api")
public interface ProdutoProducer {

}
