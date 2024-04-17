package br.com.fiap.mspedidos.infra.swagger.annotations;

import br.com.fiap.mspedidos.infra.swagger.annotations.responses.ApiResponseBadRequestJson;
import br.com.fiap.mspedidos.infra.swagger.annotations.responses.ApiResponseNoContentJson;
import br.com.fiap.mspedidos.infra.swagger.annotations.responses.ApiResponseNotFoundJson;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ METHOD })
@ApiResponseNoContentJson
@ApiResponseNotFoundJson
@ApiResponseBadRequestJson
public @interface ApiResponseSwaggerNoContent {}
