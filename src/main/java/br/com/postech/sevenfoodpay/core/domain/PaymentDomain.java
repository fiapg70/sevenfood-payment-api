package br.com.postech.sevenfoodpay.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDomain implements Serializable {

    private Long id;

    private String clientId;

    private Long paymentId;

    private String paymentStatus;

    private String paymentDetails;

    private LocalDateTime paymentDate;

    private BigDecimal paymentAmount;

    private String qrCode;

    private String qrCodeBase64;

    private String orderId;
}