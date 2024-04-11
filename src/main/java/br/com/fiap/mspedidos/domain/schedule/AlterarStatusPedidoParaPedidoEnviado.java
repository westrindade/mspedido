package br.com.fiap.mspedidos.domain.schedule;

import br.com.fiap.mspedidos.domain.dto.PedidoDtoResponse;
import br.com.fiap.mspedidos.domain.entities.PedidoEntity;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.expections.BusinessException;
import br.com.fiap.mspedidos.domain.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AlterarStatusPedidoParaPedidoEnviado {

    @Autowired
    PedidoService pedidoService;

    private final long CINCO_SEGUNDO = 5000;

    @Scheduled(fixedRate = CINCO_SEGUNDO)
    public void iniciar() {

        System.out.println("Iniciando AlterarStatusPedidoParaPedidoEnviado [" + LocalDateTime.now() + "]");

        try {
            List<PedidoDtoResponse> pedidoDtoResponseList = pedidoService.listarPedidosPorStatus(StatusPedidoEnum.AGUARDANDO_ENVIO);
            pedidoDtoResponseList.parallelStream().forEach(this::executar);
        } catch ( BusinessException  ex) {
            System.err.println("iniciar - Erro [ " + ex.getMessage() + "]");
        }
    }

    private void executar(PedidoDtoResponse pedidoDtoResponse) {
        PedidoEntity pedido = new PedidoEntity(pedidoDtoResponse.id());
        try {
            pedido.enviarPedido();
            pedidoService.enviarPedito(pedido.getId());
        } catch ( BusinessException  ex) {
            System.err.println("executar - Erro [ " + ex.getMessage() + "]");
        }
    }
}
