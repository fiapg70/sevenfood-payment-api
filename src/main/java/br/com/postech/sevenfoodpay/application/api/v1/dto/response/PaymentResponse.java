package br.com.postech.sevenfoodpay.application.api.v1.dto.response;

public record PaymentResponse(Long id, String status, String detail, String qrCodeBase64, String qrCode) {
}
