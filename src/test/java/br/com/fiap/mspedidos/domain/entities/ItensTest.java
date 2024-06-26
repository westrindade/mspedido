package br.com.fiap.mspedidos.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.com.fiap.estrutura.exception.BusinessException;

class ItensTest {
    @Test
    void naoDeveCriarItemSemProdutoInformado() {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new ItemEntity(0L,1L)

        );
        // Act e Assert
        assertEquals("Produto não informado", throwable.getMessage());
    }
    @Test
    void naoDeveCriarItemSemQuantidadeProdutoInformado() {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new ItemEntity(1L,0L)

        );
        // Act e Assert
        assertEquals("Quantidade deve ser maior que zero", throwable.getMessage());
    }
    @Test
    void naoDeveCriarItemSemPedidoInformado() {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new ItemEntity(1L,1L).informarPedido(null)

        );
        // Act e Assert
        assertEquals("Pedido deve ser informado", throwable.getMessage());
    }

    @Test
    void deveCriarItem() throws BusinessException {
        //Arrange
        ItemEntity itemOK = new ItemEntity(1L,1L);
        itemOK.informarPedido(new PedidoEntity(1L,StatusPedidoEnum.AGUARDANDO_PAGAMENTO));
        // Act e Assert
        assertNotNull(itemOK);
    }

    @Test
    void deveCriarItemSemValores() throws BusinessException {
        //Arrange
        ItemEntity itemOk = new ItemEntity();
        // Act e Assert
        assertNotNull(itemOk);
    }

    @Test
    void naoDeveSomarValorTotal() throws BusinessException {
        ItemEntity itemOK = new ItemEntity(1L,1L);
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                itemOK.setValorTotal(BigDecimal.ZERO)
        );
        // Act e Assert
        assertEquals("Valor informado não pode ser zero", throwable.getMessage());
    }
}
