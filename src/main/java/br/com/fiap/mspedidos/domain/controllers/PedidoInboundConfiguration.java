package br.com.fiap.mspedidos.domain.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

@Configuration
public class PedidoInboundConfiguration {

    @Bean
    public MessageChannel confirmarEntrega() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inbound() {
        return IntegrationFlow.from(
                        Http.inboundGateway("/api/confirmar-entrega/{id}")
                                .requestMapping(m -> m.methods(HttpMethod.PUT))
                                .statusCodeFunction(m -> HttpStatus.NO_CONTENT)
                                . payloadExpression("#pathVariables.id")
                )
                .channel("confirmarEntrega")
                .get()
                ;
    }
}
