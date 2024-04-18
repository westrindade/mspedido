package br.com.fiap.mspedidos.domain.excections;

public class BusinessException extends Exception{
    public BusinessException(final String message){
        super(message);
    }
}
