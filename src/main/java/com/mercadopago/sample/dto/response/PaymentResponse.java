package com.mercadopago.sample.dto.response;

public record PaymentResponse(Long id, String status, String detail, String qrCodeBase64, String qrCode) {
}
