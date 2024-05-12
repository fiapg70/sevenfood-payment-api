package br.com.postech.sevenfoodpay.application.api.v1.dto.response;

public record ClientResponse(
        String id, String firstName, String lastName, String documentType, String documentNumber, String email
) {
}
