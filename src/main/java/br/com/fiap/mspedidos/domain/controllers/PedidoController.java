package br.com.fiap.mspedidos.domain.controllers;

import br.com.fiap.mspedidos.domain.dto.PedidoDtoRequest;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.excections.BusinessException;
import br.com.fiap.mspedidos.domain.service.PedidoService;
import br.com.fiap.mspedidos.infra.handler.MessageErrorHandler;
import br.com.fiap.mspedidos.infra.swagger.annotations.ApiResponseSwaggerCreate;
import br.com.fiap.mspedidos.infra.swagger.annotations.ApiResponseSwaggerNoContent;
import br.com.fiap.mspedidos.infra.swagger.annotations.ApiResponseSwaggerOk;
import br.com.fiap.mspedidos.infra.swagger.annotations.responses.ApiResponseNotFoundJson;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.mspedidos.infra.Utils;

@RestController
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable Long id) {
        return Utils.response(HttpStatus.OK,
                () -> pedidoService.buscarPedidoPorId(id)
        );
    }

    @GetMapping("/cliente/{id}")
    @Operation(summary = "Buscar produto por ID de cliente")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> listarPedidosPorCliente(@PathVariable Long id) {
        return Utils.response(HttpStatus.OK,
                () -> pedidoService.listarPedidosPorCliente(id)
        );
    }
    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar produto por status")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> listarPedidosPorStatus(@PathVariable StatusPedidoEnum status) {
        return Utils.response(HttpStatus.OK,
                () -> pedidoService.listarPedidosPorStatus(status)
        );
    }
    @PostMapping
    @Operation(summary = "Cadastrar um novo pedido")
    @ApiResponseSwaggerCreate
    public ResponseEntity<?> criarPedido(@RequestBody PedidoDtoRequest pedido){
        return Utils.response(HttpStatus.CREATED,
                () -> pedidoService.criar(pedido)
        );
    }
    @PutMapping("/{id}/confirmar-pagamento")
    @Operation(summary = "Confirmar pagamento")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> confirmarPagamento(@PathVariable Long id){
        return Utils.response(HttpStatus.OK,
                () -> {
                    pedidoService.confirmarPagamento(id);
                    return "Pagamento Confirmado";
                });
    }
    @PutMapping("/{id}/cancelar-pedido")
    @Operation(summary = "Cancelar pedido")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id){
        return Utils.response(HttpStatus.OK,
                () -> {
                    pedidoService.cancelarPedido(id);
                    return "Pagamento Cancelado";
                });
    }
}

