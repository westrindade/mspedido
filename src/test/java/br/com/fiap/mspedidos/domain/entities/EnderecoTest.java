package br.com.fiap.mspedidos.domain.entities;

import br.com.fiap.estrutura.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {
    @Test
    void naoDeveCriarEnderecoSemCepInformado() throws BusinessException {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new EnderecoPedido("",
                        "Rua Céu",
                        "416",
                        "",
                        "Green Hills",
                        "Pirapora",
                        "SP")
        );
        // Act e Assert
        assertEquals("Cep não informado", throwable.getMessage());
    }
    @Test
    void naoDeveCriarEnderecoSemLogradouroInformado() throws BusinessException {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new EnderecoPedido("06550-000",
                        "",
                        "416",
                        "",
                        "Green Hills",
                        "Pirapora",
                        "SP")
        );
        // Act e Assert
        assertEquals("Logradouro não informado", throwable.getMessage());
    }
    @Test
    void naoDeveCriarEnderecoSemBairroInformado() throws BusinessException {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new EnderecoPedido("06550-000",
                        "Rua Céu",
                        "416",
                        "",
                        "",
                        "Pirapora",
                        "SP")
        );
        // Act e Assert
        assertEquals("Bairro não informado", throwable.getMessage());
    }
    @Test
    void naoDeveCriarEnderecoSemNumeroInformado() throws BusinessException {
        //Arrange
        final Throwable throwable = assertThrows(BusinessException.class, () ->
                new EnderecoPedido("06550-000",
                        "Rua Céu",
                        "",
                        "",
                        "Green Hills",
                        "Pirapora",
                        "SP")
        );
        // Act e Assert
        assertEquals("Número não informado", throwable.getMessage());
    }

    @Test
    void deveCriarEndereco() throws BusinessException {
        //Arrange
        EnderecoPedido enderecoPedidoOk = new EnderecoPedido("06550-000",
                "Rua Céu",
                "416",
                "",
                "Green Hills",
                "Pirapora",
                "SP"
        );
        // Act e Assert
        assertNotNull(enderecoPedidoOk);
    }
    @Test
    void deveCriarEnderecoSemParametro() throws BusinessException {
        //Arrange
        EnderecoPedido enderecoPedidoOk = new EnderecoPedido();
        // Act e Assert
        assertNotNull(enderecoPedidoOk);
    }
}
