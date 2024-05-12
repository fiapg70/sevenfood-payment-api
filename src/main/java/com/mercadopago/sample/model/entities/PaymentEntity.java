package com.mercadopago.sample.model.entities;

import com.mercadopago.sample.model.domain.AuditDomain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_payment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity extends AuditDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotNull(message = "o campo \"clientId\" é obrigario")
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "payment_details", nullable = false)
    private String paymentDetails;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "payment_amount", nullable = false)
    private BigDecimal paymentAmount;

    @Column(name = "qr_code", nullable = false)
    private String qrCode;

    @Column(name = "qr_code_base64", nullable = false)
    private String qrCodeBase64;

    @Column(name = "orders", nullable = false)
    private String orders;
}
