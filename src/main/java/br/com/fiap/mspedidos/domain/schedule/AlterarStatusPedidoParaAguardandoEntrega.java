package br.com.fiap.mspedidos.domain.schedule;

import br.com.fiap.mspedidos.domain.adapter.LogisticaPedidoProducer;
import br.com.fiap.mspedidos.domain.dto.PedidoLogisticaDtoRequest;
import br.com.fiap.mspedidos.domain.entities.StatusPedidoEnum;
import br.com.fiap.estrutura.exception.BusinessException;
import br.com.fiap.mspedidos.domain.service.PedidoService;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AlterarStatusPedidoParaAguardandoEntrega {

    private final PedidoService pedidoService;

    private final LogisticaPedidoProducer logisticaPedidoProducer;

    public AlterarStatusPedidoParaAguardandoEntrega(PedidoService pedidoService, LogisticaPedidoProducer logisticaPedidoProducer) {
        this.pedidoService = pedidoService;
        this.logisticaPedidoProducer = logisticaPedidoProducer;
    }

    private final long SEGUNDOS = 1000;

    @Scheduled(fixedRate = SEGUNDOS)
    public void iniciar() {

        System.out.println("Iniciando AlterarStatusPedidoParaAguardandoEntrega [" + LocalDateTime.now() + "]");

        try {
            List<PedidoLogisticaDtoRequest> pedidoList = pedidoService.listarPedidosPorStatusSchedule(StatusPedidoEnum.PAGO);
            pedidoList.parallelStream().forEach(this::executar);
        } catch ( BusinessException  ex) {
            System.err.println("iniciar - Erro [ " + ex.getMessage() + "]");
        }
    }

    private void executar(PedidoLogisticaDtoRequest pedido) {
        try {
            this.enviarLogistica(pedido);
            pedidoService.alterarStatusPedidoParaAguardarEntrega(pedido.codigoPedido());
        } catch ( BusinessException  ex) {
            System.err.println("executar - Erro [ " + ex.getMessage() + "]");
        }
    }

    private void enviarLogistica(PedidoLogisticaDtoRequest pedido) {
        this.logisticaPedidoProducer.enviarLogistica(pedido);
    }


}
