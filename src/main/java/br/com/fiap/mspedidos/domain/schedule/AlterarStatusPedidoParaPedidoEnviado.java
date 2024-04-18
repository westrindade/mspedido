package br.com.fiap.mspedidos.domain.schedule;

import br.com.fiap.mspedidos.domain.dto.PedidoDtoResponse;
import br.com.fiap.mspedidos.domain.entities.PedidoEntity;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.excections.BusinessException;
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

    private final long SEGUNDOS = 1000;

    @Scheduled(fixedRate = SEGUNDOS)
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
        PedidoEntity pedido = new PedidoEntity(pedidoDtoResponse.id(),pedidoDtoResponse.statusPedido());
        try {
            pedido.enviarPedido();
            pedidoService.enviarPedido(pedido.getId());
        } catch ( BusinessException  ex) {
            System.err.println("executar - Erro [ " + ex.getMessage() + "]");
        }
    }
}
