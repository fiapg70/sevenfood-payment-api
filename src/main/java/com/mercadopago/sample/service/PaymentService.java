package com.mercadopago.sample.service;

import com.mercadopago.sample.dto.request.PaymentRequest;
import com.mercadopago.sample.dto.response.PaymentResponse;

import java.util.Optional;

public interface PaymentService {
    Optional<PaymentResponse> processPayment(PaymentRequest paymentRequest);
}
