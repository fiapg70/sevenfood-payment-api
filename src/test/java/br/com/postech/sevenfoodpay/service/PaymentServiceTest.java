package br.com.postech.sevenfoodpay.service;

import br.com.postech.sevenfoodpay.application.api.v1.dto.request.OrderDTO;
import br.com.postech.sevenfoodpay.application.api.v1.dto.request.PaymentRequest;
import br.com.postech.sevenfoodpay.application.api.v1.dto.response.ClientResponse;
import br.com.postech.sevenfoodpay.application.api.v1.dto.response.PaymentResponse;
import br.com.postech.sevenfoodpay.application.database.mapper.PaymentMapper;
import br.com.postech.sevenfoodpay.core.domain.PaymentDomain;
import br.com.postech.sevenfoodpay.core.ports.out.PaymentRepositoryPort;
import br.com.postech.sevenfoodpay.core.service.PaymentService;
import br.com.postech.sevenfoodpay.gateway.client.ClientWebClient;
import br.com.postech.sevenfoodpay.infraestruture.entities.PaymentEntity;
import br.com.postech.sevenfoodpay.infraestruture.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepositoryPort paymentRepositoryPort;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    PaymentMapper mapper;

    @Mock
    ClientWebClient restClient;

    @Value("${mercado_pago_sample_access_token}")
    private String mercadoPagoAccessToken;

    private PaymentEntity getPaymentEntity() {
        return PaymentEntity.builder()
                .clientId("1")
                .paymentId(1L)
                .paymentStatus("PENDING")
                .paymentDetails("Payment pending")
                .paymentDate(LocalDateTime.now())
                .paymentAmount(new BigDecimal(100))
                .qrCode("123456")
                .qrCodeBase64("123456")
                .orders("1")
                .build();
    }

    private PaymentDomain getPaymentoDomain() {
        return PaymentDomain.builder()
                .clientId("1")
                .paymentId(1L)
                .paymentStatus("PENDING")
                .paymentDetails("Payment pending")
                .paymentDate(LocalDateTime.now())
                .paymentAmount(new BigDecimal(100))
                .qrCode("123456")
                .qrCodeBase64("123456")
                .orders("1")
                .build();
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void savePaymentTest() {
        // Mockando o valor de mercadoPagoAccessToken
        when(mercadoPagoAccessToken).thenReturn(mercadoPagoAccessToken);

        List<PaymentDomain> paymentDomainList = List.of(getPaymentoDomain());
        PaymentDomain paymentDomain = getPaymentoDomain();

        PaymentEntity paymentEntity = getPaymentEntity();
        List<PaymentEntity> paymentEntityList = List.of(paymentEntity);

        when(mapper.map(paymentEntityList)).thenReturn(paymentDomainList);
        when(paymentRepositoryPort.save(getPaymentoDomain())).thenReturn(getPaymentoDomain());

        when(paymentRepositoryPort.save(getPaymentoDomain())).thenReturn(getPaymentoDomain());

        ClientResponse clientResponse = new ClientResponse("76fb2f44-aaad-4ccd-a685-4b2dd33ef1c2",
                "John Doe",
                "Silva",
                "CPF",
                "04358243681", "fontestz@gmail.com");
        when(restClient.getUserById("76fb2f44-aaad-4ccd-a685-4b2dd33ef1c2")).thenReturn(clientResponse);

        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderDTOList.add(new OrderDTO("ORD-001"));
        orderDTOList.add(new OrderDTO("ORD-002"));

        PaymentRequest paymentRequest = new PaymentRequest(
                BigDecimal.valueOf(0.01),
                "Combo X Salada",
                "76fb2f44-aaad-4ccd-a685-4b2dd33ef1c2",
                orderDTOList
        );

        Optional<PaymentResponse> paymentDomainSaved = paymentService.processPayment(paymentRequest);
        assertNotNull(paymentDomainSaved.get());
    }

}