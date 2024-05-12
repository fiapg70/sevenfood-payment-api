package com.mercadopago.sample.api.v1;

import com.mercadopago.sample.dto.request.PaymentRequest;
import com.mercadopago.sample.dto.response.PaymentResponse;
import com.mercadopago.sample.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/payments")
public class PaymentResource {

    private static final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PaymentResponse> save(@Valid @RequestBody PaymentRequest request) {
        log.info("Objeto Recebido na requisição: {}", request);
        Optional<PaymentResponse> paymentResponse = paymentService.processPayment(request);
        if (paymentResponse.isPresent()) {
            return ResponseEntity.ok(paymentResponse.get());
        }
        return ResponseEntity.notFound().build();
    }
}
