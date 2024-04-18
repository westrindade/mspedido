package br.com.fiap.mspedidos.domain.controllers;

import br.com.fiap.mspedidos.domain.dto.PedidoDtoRequest;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.expections.BusinessException;
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

@RestController
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    @ApiResponseSwaggerOk
    @ApiResponseSwaggerNoContent
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pedidoService.buscarPedidoPorId(id));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageErrorHandler.create(e.getMessage()));
        }
    }

    @GetMapping("/cliente/{id}")
    @Operation(summary = "Buscar produto por ID de cliente")
    @ApiResponseSwaggerOk
    @ApiResponseSwaggerNoContent
    public ResponseEntity<?> listarPedidosPorCliente(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pedidoService.listarPedidosPorCliente(id));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageErrorHandler.create(e.getMessage()));
        }
    }
    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar produto por status")
    @ApiResponseSwaggerOk
    @ApiResponseSwaggerNoContent
    public ResponseEntity<?> listarPedidosPorStatus(@PathVariable StatusPedidoEnum status) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pedidoService.listarPedidosPorStatus(status));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageErrorHandler.create(e.getMessage()));
        }
    }
    @PostMapping
    @Operation(summary = "Cadastrar um novo pedido")
    @ApiResponseSwaggerCreate
    @ApiResponseNotFoundJson
    public ResponseEntity<?> criarPedido(@RequestBody PedidoDtoRequest pedido){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criar(pedido));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageErrorHandler.create(e.getMessage()));
        }
    }
    @PutMapping("/{id}/confirmar-pagamento")
    @Operation(summary = "Confirmar pagamento")
    @ApiResponseSwaggerCreate
    @ApiResponseNotFoundJson
    public ResponseEntity<?> confirmarPagamento(@PathVariable Long id){
        try {
            pedidoService.confirmarPagamento(id);
            return ResponseEntity.status(HttpStatus.OK).body("Pagamento Confirmado");
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageErrorHandler.create(e.getMessage()));
        }
    }
    @PutMapping("/{id}/cancelar-pedido")
    @Operation(summary = "Cancelar pedido")
    @ApiResponseSwaggerCreate
    @ApiResponseNotFoundJson
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id){
        try {
            pedidoService.cancelarPedido(id);
            return ResponseEntity.status(HttpStatus.OK).body("Pedido Cancelado");
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageErrorHandler.create(e.getMessage()));
        }
    }
}

