package br.com.postech.sevenfoodpay.commons.exception;

public class MercadoPagoException extends RuntimeException {
    public MercadoPagoException(String message) {
        super(message);
    }
}
