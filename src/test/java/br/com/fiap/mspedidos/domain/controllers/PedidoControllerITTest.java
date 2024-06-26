package br.com.fiap.mspedidos.domain.controllers;

import br.com.fiap.mspedidos.domain.dto.EnderecoDtoRequest;
import br.com.fiap.mspedidos.domain.dto.ItemPedidoDtoRequest;
import br.com.fiap.mspedidos.domain.dto.PedidoDtoRequest;
import br.com.fiap.mspedidos.domain.entities.FormaPagamentoEnum;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Profile("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PedidoControllerITTest {
    @LocalServerPort
    private int porta;

    @BeforeEach
    void setUp() {
        RestAssured.port = porta;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void deveVerificarSeClientePossuiPedidos() {
        given()
            .formParam("codigoCliente", 1)
        .when()
            .post("cliente/possui-pedidos")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("possui-pedidos", is(true));
    }

    @Test
    void deveVerificarSeClienteNaoPossuiPedidos() {
        given()
            .formParam("codigoCliente", 99)
        .when()
            .post("cliente/possui-pedidos")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("possui-pedidos", is(false));
    }

    @Test
    void deveListarPedidosPorCliente() {
        given()
                .pathParam("id", 1)
                .when()
                .get("/cliente/{id}")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    void naoDeveListarPedidosPorCliente() {
        given()
                .pathParam("id", 50000)
                .when()
                .get("/cliente/{id}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                ;
    }
    @Test
    void deveListarPedidosPorStatus() {
        given()
                .pathParam("status", "AGUARDANDO_PAGAMENTO")
                .when()
                .get("/status/{status}")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    void naoDeveListarPedidosPorStatus() {
        given()
                .pathParam("status", "PAGO")
                .when()
                .get("/status/{status}")
                .then()
                .statusCode(HttpStatus.SC_OK)
        ;
    }
    @Test
    void deveBuscarPedidoPorId() {
        given()
                .pathParam("id", 10)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    void naoDeveBuscarPedidoPorIdInexistente() {
        given()
                .pathParam("id", 50000)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Pedido não encontrado"));
    }
    @Test
    void naoConectouComMsCliente() {
        given()
                .contentType(ContentType.JSON)
                .body(new CriarObjetosDto().criarPedidoDtoRequest())
                .when()
                .post("/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Conexão com msCliente não estabelecida"));
    }
    @Test
    void naoConectouComMsProduto() {
        given()
                .contentType(ContentType.JSON)
                .body(new CriarObjetosDto().criarPedidoDtoRequest())
                .when()
                .post("/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Conexão com msProduto não estabelecida"));
    }

    @Test
    void deveCadastrarPedido() {
        given()
            .contentType(ContentType.JSON)
            .body(new CriarObjetosDto().criarPedidoDtoRequest())
            .when()
                .post("/")
            .then()
                .statusCode(HttpStatus.SC_CREATED)
        .body("id", is(1));
//                .log()
        ;
    }

    @Nested
    class CriarObjetosDto {
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
