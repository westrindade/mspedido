package br.com.fiap.mspedidos.domain.controllers;

import br.com.fiap.mspedidos.domain.excections.BusinessException;
@FunctionalInterface
public interface GerarResponse<T> {

	T get() throws BusinessException;
}
