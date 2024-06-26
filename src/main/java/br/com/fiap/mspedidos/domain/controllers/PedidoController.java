package br.com.fiap.mspedidos.domain.controllers;

import br.com.fiap.estrutura.swagger.annotations.ApiResponseSwaggerCreate;
import br.com.fiap.estrutura.swagger.annotations.ApiResponseSwaggerOk;
import br.com.fiap.estrutura.utils.SpringControllerUtils;
import br.com.fiap.mspedidos.domain.dto.PedidoDtoRequest;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable Long id) {
        return SpringControllerUtils.response(HttpStatus.OK,
                () -> pedidoService.buscarPedidoPorId(id)
        );
    }

    @GetMapping("/cliente/{id}")
    @Operation(summary = "Buscar pedido por ID de cliente")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> listarPedidosPorCliente(@PathVariable Long id) {
        return SpringControllerUtils.response(HttpStatus.OK,
                () -> pedidoService.listarPedidosPorCliente(id)
        );
    }
    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar pedido por status")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> listarPedidosPorStatus(@PathVariable StatusPedidoEnum status) {
        return SpringControllerUtils.response(HttpStatus.OK,
                () -> pedidoService.listarPedidosPorStatus(status)
        );
    }
    @PostMapping
    @Operation(summary = "Cadastrar um novo pedido")
    @ApiResponseSwaggerCreate
    public ResponseEntity<?> criarPedido(@RequestBody PedidoDtoRequest pedido){
        return SpringControllerUtils.response(HttpStatus.CREATED,
                () -> pedidoService.criar(pedido)
        );
    }
    @PutMapping("/{id}/confirmar-pagamento")
    @Operation(summary = "Confirmar pagamento")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> confirmarPagamento(@PathVariable Long id){
        return SpringControllerUtils.response(HttpStatus.OK,
                () -> {
                    pedidoService.pagar(id);
                    return "Pagamento Confirmado";
                });
    }
    @PutMapping("/{id}/cancelar-pedido")
    @Operation(summary = "Cancelar pedido")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id){
        return SpringControllerUtils.response(HttpStatus.OK,
                () -> {
                    pedidoService.cancelarPedido(id);
                    return "Pagamento Cancelado";
                });
    }

    @PutMapping("/{id}/confirmar-entrega")
    @Operation(summary = "Confirmar entrega pedido")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> entregarPedido(@PathVariable Long id){
        return SpringControllerUtils.response(HttpStatus.OK,
                () -> {
                    pedidoService.entregarPedido(id);
                    return "Pedido Entregue";
                });
    }

    @PostMapping("/cliente/possui-pedidos")
    @Operation(summary = "verifica se cliente possui pedidos no serviço")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> listarPedidosPorCliente(@RequestParam long codigoCliente) {
        return SpringControllerUtils.response(HttpStatus.OK, () -> {
            return Map.of("possui-pedidos", pedidoService.clientePossuiPedidos(codigoCliente));
        });
    }
}

