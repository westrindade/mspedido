package br.com.fiap.mspedidos.domain.entities;

import br.com.fiap.mspedidos.domain.dto.PedidoDtoResponse;
import br.com.fiap.mspedidos.domain.exceptions.BusinessException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {
    @Test
    void naoDeveCriarPedidoSemClienteInformado() throws BusinessException {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new PedidoEntity(0L,
                        FormaPagamentoEnum.PIX,
                        1,
                        new CriarObjetos().criarListItem(),
                        new CriarObjetos().criarEndereco()
                )

        );
        // Act e Assert
        assertEquals("Cliente não informado", throwable.getMessage());
    }
    @Test
    void naoDeveCriarPedidoSemEnderecoInformado() throws BusinessException {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new PedidoEntity(1L,
                        FormaPagamentoEnum.PIX,
                        1,
                        new CriarObjetos().criarListItem(),
                        null
                )

        );
        // Act e Assert
        assertEquals("Endereço do pedido não informado", throwable.getMessage());
    }
    @Test
    void naoDeveCriarPedidoSemItensInformado() throws BusinessException {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new PedidoEntity(1L,
                        FormaPagamentoEnum.PIX,
                        1,
                        null,
                        new CriarObjetos().criarEndereco()
                )

        );
        // Act e Assert
        assertEquals("Itens do pedido não informado", throwable.getMessage());
    }
    @Test
    void naoDeveCriarPedidoSemQtdeParcelasInformado() throws BusinessException {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new PedidoEntity(1L,
                        FormaPagamentoEnum.PIX,
                        0,
                        new CriarObjetos().criarListItem(),
                        new CriarObjetos().criarEndereco()
                )

        );
        // Act e Assert
        assertEquals("Quantidade de Parcelas do Pedido não informada", throwable.getMessage());
    }
    @Test
    void deveCriarPedido() throws BusinessException {
        //Arrange
        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
        // Act e Assert
        assertNotNull(pedidoOk);
    }

    @Test
    void deveCriarPedidoSemValores() throws BusinessException {
        //Arrange
        PedidoEntity pedidoOk = new PedidoEntity();
        // Act e Assert
        assertNotNull(pedidoOk);
    }
    @Test
    void deveConfirmarPagamentoPedido() throws BusinessException {
        //Arrange
        PedidoEntity pedidoOk = new PedidoEntity(1L,
                FormaPagamentoEnum.PIX,
                1,
                new CriarObjetos().criarListItem(),
                new CriarObjetos().criarEndereco()
        );
        pedidoOk.pagarPedido();
        // Act e Assert
        assertNotNull(pedidoOk);
    }
    @Test
    void naoDeveCancelarPedido() throws BusinessException {
        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
        pedidoOk.cancelarPedido();
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                pedidoOk.cancelarPedido()
        );
        // Act e Assert
        assertEquals("Pedido não pode ser cancelado", throwable.getMessage());
    }
    @Test
    void naoDeveEnviarPedido() throws BusinessException {
        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                pedidoOk.entregarPedido()
        );
        // Act e Assert
        assertEquals("Pedido não pode ser entregue", throwable.getMessage());
    }
    @Test
    void naoDeveAguardarEntregaPedido() throws BusinessException {
        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                pedidoOk.aguardarEntrega()
        );
        // Act e Assert
        assertEquals("Pedido não pode ser direcionado para aguardando entrega", throwable.getMessage());
    }
    @Test
    void naoDeveSomarValorTotal() throws BusinessException {
        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                pedidoOk.somarValorTotal(BigDecimal.ZERO)
        );
        // Act e Assert
        assertEquals("Valor informado não pode ser zero", throwable.getMessage());
    }
    @Test
    void deveCancelarPedido() throws BusinessException {
        //Arrange
        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
        pedidoOk.cancelarPedido();
        // Act e Assert
        assertNotNull(pedidoOk);
    }
//    @Test
//    void deveEnviarPedido() throws BusinessException {
//        //Arrange
//        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
//        pedidoOk.aguardarEntrega();
//        pedidoOk.enviarPedido();
//        // Act e Assert
//        assertNotNull(pedidoOk);
//    }
//    @Test
//    void deveAguardarEntregaPedido() throws BusinessException {
//        //Arrange
//        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
//        pedidoOk.aguardarEnvioPedido();
//        // Act e Assert
//        assertNotNull(pedidoOk);
//    }
    @Test
    void deveSomarValorTotal() throws BusinessException {
        //Arrange
        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
        pedidoOk.somarValorTotal(new BigDecimal(1));
        // Act e Assert
        assertNotNull(pedidoOk);
    }
    @Test
    void deveRetornarDto() throws BusinessException {
        //Arrange
        PedidoEntity pedidoOk = new CriarObjetos().criarPedidoOK();
        PedidoDtoResponse pedidoDtoResponseOK = pedidoOk.toDto();
        // Act e Assert
        assertNotNull(pedidoOk);
        assertNotNull(pedidoDtoResponseOK);
    }

    @Nested
    class CriarObjetos {
        private List<ItemEntity> criarListItem() throws BusinessException {
            List<ItemEntity> itemList = new ArrayList<>();
            itemList.add(new ItemEntity(1L,1L));
            return itemList;
        }

        private EnderecoPedido criarEndereco() throws BusinessException {
            return new EnderecoPedido("06550-000","Rua Céu","416","","Green Hills","Pirapora","SP");
        }

        private PedidoEntity criarPedidoOK() throws BusinessException {
            return new PedidoEntity(1L,
                    FormaPagamentoEnum.PIX,
                    1,
                    criarListItem(),
                    criarEndereco()
            );
        }
    }

}
