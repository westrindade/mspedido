package br.com.fiap.mspedidos.domain.service;

import br.com.fiap.mspedidos.domain.adapter.EstoquePedidoProducer;
import br.com.fiap.mspedidos.domain.adapter.ProdutoPedidoProducer;
import br.com.fiap.mspedidos.domain.dto.*;
import br.com.fiap.mspedidos.domain.entities.*;
import br.com.fiap.mspedidos.domain.exceptions.BusinessException;
import br.com.fiap.mspedidos.domain.repositories.PedidoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.CollectionAssert.assertThatCollection;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {
    @InjectMocks
    private PedidoService service;
    @Mock
    private PedidoRepository repository;
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
    void deveRetornarListaDePedidosPorCliente() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedido();
        when(repository.findByIdCliente(anyLong())).thenReturn(Arrays.asList(pedido,pedido));
        // Act
        final List<PedidoDtoResponse> pedidos = service.listarPedidosPorCliente(1L);
        // Assert
        assertNotNull(pedidos);
        assertThatCollection(pedidos)
                .hasSize(2)
                .extracting(PedidoDtoResponse::id);
        verify(repository, times(1)).findByIdCliente(anyLong());
    }
    @Test
    void deveRetornarListaDePedidosPorStatus() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedido();
        when(repository.findByStatusPedido(any(StatusPedidoEnum.class))).thenReturn(Arrays.asList(pedido,pedido));
        // Act
        final List<PedidoDtoResponse> pedidos = service.listarPedidosPorStatus(StatusPedidoEnum.PREPARANDO);
        // Assert
        assertNotNull(pedidos);
        assertThatCollection(pedidos)
                .hasSize(2)
                .extracting(PedidoDtoResponse::id);
        verify(repository, times(1)).findByStatusPedido(any(StatusPedidoEnum.class));
    }
    @Test
    void deveRetornarPedidosPorId() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedido();
        when(repository.findById(anyLong())).thenReturn(Optional.of(pedido));
        // Act
        final PedidoDtoResponse pedidoDtoResponse = service.buscarPedidoPorId(1L);
        // Assert
        assertNotNull(pedidoDtoResponse);
        verify(repository, times(1)).findById(anyLong());
    }
    @Test
    void deveCriarPedido() throws BusinessException {
        //Arrange
        PedidoDtoRequest pedidoDtoRequest = new CriarObjetosDto().criarPedidoDtoRequest();
        PedidoEntity pedido = new CriarObjetosEntity().criarPedido();

        doNothing().when(estoquePedidoProducer).removerEstoque(anyLong(),anyLong());
        when(produtoPedidoProducer.obterProduto(anyLong())).thenReturn(new CriarObjetosDto().criarProdutoDtoResponse());
        when(repository.save(any(PedidoEntity.class))).thenReturn(pedido);
        // Act
        final PedidoDtoResponse pedidoDtoResponse = service.criar(pedidoDtoRequest);
        // Assert
        assertNotNull(pedidoDtoResponse);
        verify(repository, times(1)).save(any(PedidoEntity.class));
    }
    @Test
    void naoDeveCriarPedidoQuandoNaoEncontrarProdutoNoMsProduto() throws BusinessException {
        //Arrange
        PedidoDtoRequest pedidoDtoRequest = new CriarObjetosDto().criarPedidoDtoRequest();
        PedidoEntity pedido = new CriarObjetosEntity().criarPedido();

        doNothing().when(estoquePedidoProducer).removerEstoque(anyLong(),anyLong());
        when(produtoPedidoProducer.obterProduto(anyLong())).thenReturn(null);
        // Act
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.criar(pedidoDtoRequest));
        // Assert
        assertEquals("Produto 1 não encontrado", throwable.getMessage());
    }

    @Test
    void naoDeveCriarPedidoQuandoNaoOProdutoNaoTemPreco() throws BusinessException {
        //Arrange
        PedidoDtoRequest pedidoDtoRequest = new CriarObjetosDto().criarPedidoDtoRequest();
        PedidoEntity pedido = new CriarObjetosEntity().criarPedido();

        doNothing().when(estoquePedidoProducer).removerEstoque(anyLong(),anyLong());
        when(produtoPedidoProducer.obterProduto(anyLong())).thenReturn(new CriarObjetosDto().criarProdutoDtoResponseSemPreco());
        // Act
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.criar(pedidoDtoRequest));
        // Assert
        assertEquals("Produto 1 não possue valor cadastrao", throwable.getMessage());
    }
    @Test
    void deveConfirmarPagamento() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComId();

        when(repository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(repository.save(any(PedidoEntity.class))).thenReturn(pedido);
        // Act
        service.confirmarPagamento(pedido.getId());
        final PedidoDtoResponse pedidoDtoResponse = service.buscarPedidoPorId(pedido.getId());
        // Assert
        assertEquals(pedidoDtoResponse.statusPagamento(), StatusPagamentoEnum.PAGAMENTO_CONFIRMADO);
        verify(repository, times(2)).findById(anyLong());
        verify(repository, times(1)).save(any(PedidoEntity.class));
    }
    @Test
    void naoDeveConfirmarPagamento() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComId();
        // Act
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.confirmarPagamento(anyLong()));
        // Assert
        assertEquals("Pedido não encontrado", throwable.getMessage());
        verify(repository, times(1)).findById(anyLong());
    }
    @Test
    void deveAlterarStatusPedidoParaAguardarEnvio() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComId();

        when(repository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(repository.save(any(PedidoEntity.class))).thenReturn(pedido);
        // Act
        service.alterarStatusPedidoParaAguardarEnvio(pedido.getId());
        final PedidoDtoResponse pedidoDtoResponse = service.buscarPedidoPorId(pedido.getId());
        // Assert
        assertEquals(pedidoDtoResponse.statusPedido(), StatusPedidoEnum.AGUARDANDO_ENVIO);
        verify(repository, times(2)).findById(anyLong());
        verify(repository, times(1)).save(any(PedidoEntity.class));
    }
    @Test
    void naoDeveAlterarStatusPedidoParaAguardarEnvio() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComId();
        // Act
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.alterarStatusPedidoParaAguardarEnvio(anyLong()));
        // Assert
        assertEquals("Pedido não encontrado", throwable.getMessage());
        verify(repository, times(1)).findById(anyLong());
    }
    @Test
    void naoDeveAlterarStatusPedidoParaAguardarEnvioPorStatusEtapaDivergente() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComIdComStatusEnviado();

        when(repository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(repository.save(any(PedidoEntity.class))).thenReturn(pedido);
        // Act
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.alterarStatusPedidoParaAguardarEnvio(anyLong()));
        // Assert
        assertEquals("Pedido não pode ser direcionado para aguardando envio", throwable.getMessage());
        verify(repository, times(1)).findById(anyLong());
    }
    @Test
    void deveEnviarPedido() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComId();

        when(repository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(repository.save(any(PedidoEntity.class))).thenReturn(pedido);
        // Act
        service.alterarStatusPedidoParaAguardarEnvio(pedido.getId());
        service.enviarPedido(pedido.getId());
        final PedidoDtoResponse pedidoDtoResponse = service.buscarPedidoPorId(pedido.getId());
        // Assert
        assertEquals(pedidoDtoResponse.statusPedido(), StatusPedidoEnum.ENVIADO);
        verify(repository, times(3)).findById(anyLong());
        verify(repository, times(2)).save(any(PedidoEntity.class));
    }
    @Test
    void naoDeveEnviarPedidoPorStatusEtapaDivergente() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComId();

        when(repository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(repository.save(any(PedidoEntity.class))).thenReturn(pedido);
        // Act
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.enviarPedido(anyLong()));
        // Assert
        assertEquals("Pedido não pode ser enviado", throwable.getMessage());
        verify(repository, times(1)).findById(anyLong());
    }
    @Test
    void naoDeveEnviarPedido() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComId();
        // Act
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.enviarPedido(anyLong()));
        // Assert
        assertEquals("Pedido não encontrado", throwable.getMessage());
        verify(repository, times(1)).findById(anyLong());
    }
    //cancelarPedido
    @Test
    void deveCancelarPedido() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComId();
        PedidoEntity pedidoCompleto = new CriarObjetosEntity().criarPedido();

        when(repository.findById(anyLong())).thenReturn(Optional.of(pedidoCompleto));
        when(repository.save(any(PedidoEntity.class))).thenReturn(pedidoCompleto);
        doNothing().when(estoquePedidoProducer).removerEstoque(anyLong(),anyLong());
        // Act
        service.cancelarPedido(pedido.getId());
        final PedidoDtoResponse pedidoDtoResponse = service.buscarPedidoPorId(pedido.getId());
        // Assert
        assertEquals(pedidoDtoResponse.statusPedido(), StatusPedidoEnum.CANCELADO);
        verify(repository, times(2)).findById(anyLong());
        verify(repository, times(1)).save(any(PedidoEntity.class));
    }
    @Test
    void naoDeveCancelarPedidoPorStatusEtapaDivergente() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComIdComStatusCancelado();

        when(repository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(repository.save(any(PedidoEntity.class))).thenReturn(pedido);
        // Act
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.cancelarPedido(anyLong()));
        // Assert
        assertEquals("Pedido não pode ser cancelado", throwable.getMessage());
        verify(repository, times(1)).findById(anyLong());
    }
    @Test
    void naoDeveCancelarPedido() throws BusinessException {
        //Arrange
        PedidoEntity pedido = new CriarObjetosEntity().criarPedidoComId();
        // Act
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.cancelarPedido(anyLong()));
        // Assert
        assertEquals("Pedido não encontrado", throwable.getMessage());
        verify(repository, times(1)).findById(anyLong());
    }

    @Nested
    class CriarObjetosEntity {
        private ItemEntity criarItem() throws BusinessException {
            ItemEntity item = new ItemEntity(1L,1L);
            item.informarPedido(new PedidoEntity(1L, StatusPedidoEnum.PREPARANDO));
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
        private PedidoEntity criarPedidoComId() throws BusinessException {
            return new PedidoEntity(1L,
                    StatusPedidoEnum.PREPARANDO
            );
        }
        private PedidoEntity criarPedidoComIdComStatusEnviado() throws BusinessException {
            return new PedidoEntity(1L,
                    StatusPedidoEnum.ENVIADO
            );
        }
        private PedidoEntity criarPedidoComIdComStatusCancelado() throws BusinessException {
            return new PedidoEntity(1L,
                    StatusPedidoEnum.CANCELADO
            );
        }
    }

    @Nested
    class CriarObjetosDto {
        private ProdutoDtoResponse criarProdutoDtoResponse(){
            return new ProdutoDtoResponse(1,"BR01","CX para embalagem",10,3.99);
        }
        private ProdutoDtoResponse criarProdutoDtoResponseSemPreco(){
            return new ProdutoDtoResponse(1,"BR01","CX para embalagem",10,0);
        }
        private ItemPedidoDtoRequest criarItemPedidoDtoRequest(){
            return new ItemPedidoDtoRequest(1L,1L);
        }
        private List<ItemPedidoDtoRequest> criarListItemPedidoDtoRequest() {
            List<ItemPedidoDtoRequest> itemPedidoDtoRequestList = new ArrayList<>();
            itemPedidoDtoRequestList.add(criarItemPedidoDtoRequest());
            return itemPedidoDtoRequestList;
        }
        private PedidoDtoRequest criarPedidoDtoRequest(){
            return new PedidoDtoRequest(
                    1L,
                    criarListItemPedidoDtoRequest(),
                    FormaPagamentoEnum.PIX,
                    1,
                    new EnderecoDtoRequest("06550-000","Rua Céu","416","","Green Hills","Pirapora","SP")
            );
        }
    }
}
