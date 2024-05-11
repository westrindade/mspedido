package br.com.fiap.mspedidos.domain.schedule;

import br.com.fiap.mspedidos.domain.adapter.EstoquePedidoProducer;
import br.com.fiap.mspedidos.domain.adapter.LogisticaPedidoProducer;
import br.com.fiap.mspedidos.domain.adapter.ProdutoPedidoProducer;
import br.com.fiap.mspedidos.domain.dto.*;
import br.com.fiap.mspedidos.domain.entities.*;
import br.com.fiap.estrutura.exception.BusinessException;
import br.com.fiap.mspedidos.domain.service.PedidoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class AlterarStatusPedidoParaAguardandoEntregaTest {
    @InjectMocks
    private AlterarStatusPedidoParaAguardandoEntrega schedule;
    @Mock
    private PedidoService service;
    @Mock
    LogisticaPedidoProducer logistica;
    @Mock
    private ProdutoPedidoProducer produtoPedidoProducer;
    @Mock
    private EstoquePedidoProducer estoquePedidoProducer;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setup(){
        autoCloseable = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void deveEnviarPedidoParaLogistica() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedido();
        pedido.pagarPedido();
        PedidoLogisticaDtoRequest pedidoLogisticaDtoRequest = pedido.toDtoLogistica();
        List<PedidoLogisticaDtoRequest> pedidoDtoList = Collections.singletonList(pedidoLogisticaDtoRequest);
        when(service.listarPedidosPorStatusSchedule(StatusPedidoEnum.PAGO)).thenReturn(pedidoDtoList);
        // Act
        schedule.iniciar();
        // Assert
        assertNotNull(pedidoDtoList);
        verify(service, times(1)).listarPedidosPorStatusSchedule(StatusPedidoEnum.PAGO);
    }

    @Test
    void naoDeveEnviarPedidoParaLogistica() throws BusinessException {
        //Arrange
        List<PedidoLogisticaDtoRequest> pedidoDtoList = null;
        when(service.listarPedidosPorStatusSchedule(StatusPedidoEnum.AGUARDANDO_PAGAMENTO)).thenReturn(pedidoDtoList);
        // Act
        schedule.iniciar();
        // Assert
        assertNull(pedidoDtoList);
        verify(service, times(1)).listarPedidosPorStatusSchedule(StatusPedidoEnum.PAGO);
    }

    @Nested
    class CriarObjetosEntity {
        private ItemEntity criarItem() throws BusinessException {
            ItemEntity item = new ItemEntity(1L,1L);
            item.informarPedido(new PedidoEntity(1L,StatusPedidoEnum.AGUARDANDO_PAGAMENTO));
            return item;
        }
        private List<ItemEntity> criarListItem() throws BusinessException {
            List<ItemEntity> itemList = new ArrayList<>();
            itemList.add(criarItem());
            return itemList;
        }
        private EnderecoPedido criarEndereco() throws BusinessException {
            return new EnderecoPedido("06550-000","Rua Céu","416","","Green Hills","Pirapora","SP");
        }
        private PedidoEntity criarPedido() throws BusinessException {
            return new PedidoEntity(1L,
                    FormaPagamentoEnum.PIX,
                    1,
                    criarListItem(),
                    criarEndereco()
            );
        }
    }
}
