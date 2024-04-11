package br.com.fiap.mspedidos.domain.expections;

public class BusinessException extends Exception{
    public BusinessException(final String message){
        super(message);
    }
}
