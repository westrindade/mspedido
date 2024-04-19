package br.com.fiap.mspedidos.domain.schedule;

import br.com.fiap.mspedidos.domain.dto.PedidoDtoResponse;
import br.com.fiap.mspedidos.domain.entities.PedidoEntity;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.mspedidos.domain.exceptions.BusinessException;
import br.com.fiap.mspedidos.domain.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AlterarStatusPedidoParaAguardandoEnvio {

    @Autowired
    PedidoService pedidoService;

    private final long SEGUNDOS = 1000;

    @Scheduled(fixedRate = SEGUNDOS)
    public void iniciar() {

        System.out.println("Iniciando AlterarStatusPedidoParaAguardandoEnvio [" + LocalDateTime.now() + "]");

        try {
            List<PedidoDtoResponse> pedidoDtoResponseList = pedidoService.listarPedidosPorStatus(StatusPedidoEnum.PREPARANDO);
            pedidoDtoResponseList.parallelStream().forEach(this::executar);
        } catch ( BusinessException  ex) {
            System.err.println("iniciar - Erro [ " + ex.getMessage() + "]");
        }
    }

    private void executar(PedidoDtoResponse pedidoDtoResponse) {
        PedidoEntity pedido = new PedidoEntity(pedidoDtoResponse.id(),pedidoDtoResponse.statusPedido());
        try {
            pedido.aguardarEnvioPedido();
            pedidoService.alterarStatusPedidoParaAguardarEnvio(pedido.getId());
        } catch ( BusinessException  ex) {
            System.err.println("executar - Erro [ " + ex.getMessage() + "]");
        }
    }
}
