package com.mercadopago.sample.dto.response;

public record ClientResponse(
        String id, String firstName, String lastName, String documentType, String documentNumber, String email
) {
}
