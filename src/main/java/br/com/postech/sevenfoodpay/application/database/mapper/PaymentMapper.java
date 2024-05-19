package br.com.postech.sevenfoodpay.application.database.mapper;

import br.com.postech.sevenfoodpay.core.domain.PaymentDomain;
import br.com.postech.sevenfoodpay.infraestruture.entities.PaymentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PaymentMapper {

    private static final Logger log = LoggerFactory.getLogger(PaymentMapper.class);

    public PaymentEntity toEntity(PaymentDomain paymentDomain) {
        Long paymentId = paymentDomain.getId() != null ? paymentDomain.getId() : 0L;
        log.info("PaymentId: {}", paymentId);
        return PaymentEntity.builder()
                .clientId(paymentDomain.getClientId())
                .paymentId(paymentId)
                .paymentStatus(paymentDomain.getPaymentStatus())
                .paymentDetails(paymentDomain.getPaymentDetails())
                .paymentDate(LocalDateTime.now())
                .paymentAmount(paymentDomain.getPaymentAmount())
                .qrCode(paymentDomain.getQrCode())
                .qrCodeBase64(paymentDomain.getQrCodeBase64())
                .orders(paymentDomain.getOrderId())
                .build();
    }

    public PaymentDomain toDomain(PaymentEntity paymentEntity) {
        return PaymentDomain.builder()
                .id(paymentEntity.getPaymentId())
                .clientId(paymentEntity.getClientId())
                .paymentStatus(paymentEntity.getPaymentStatus())
                .paymentDetails(paymentEntity.getPaymentDetails())
                .paymentDate(paymentEntity.getPaymentDate())
                .paymentAmount(paymentEntity.getPaymentAmount())
                .qrCode(paymentEntity.getQrCode())
                .qrCodeBase64(paymentEntity.getQrCodeBase64())
                .orderId(paymentEntity.getOrders())
                .build();
    }

    public List<PaymentDomain> map(List<PaymentEntity> allPayments) {
        return allPayments.stream().map(this::toDomain).toList();
    }
}
