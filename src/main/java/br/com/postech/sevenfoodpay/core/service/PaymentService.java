package br.com.postech.sevenfoodpay.core.service;

import br.com.postech.sevenfoodpay.application.api.v1.dto.request.PaymentRequest;
import br.com.postech.sevenfoodpay.application.api.v1.dto.response.ClientResponse;
import br.com.postech.sevenfoodpay.application.api.v1.dto.response.PaymentResponse;
import br.com.postech.sevenfoodpay.application.api.v1.resources.PaymentResource;
import br.com.postech.sevenfoodpay.core.domain.PaymentDomain;
import br.com.postech.sevenfoodpay.core.ports.out.PaymentRepositoryPort;
import br.com.postech.sevenfoodpay.gateway.dto.OrderResponse;
import br.com.postech.sevenfoodpay.infraestruture.repository.PaymentRepository;
import br.com.postech.sevenfoodpay.commons.exception.MercadoPagoException;
import br.com.postech.sevenfoodpay.core.dto.*;
import br.com.postech.sevenfoodpay.core.ports.in.PaymentPort;
import br.com.postech.sevenfoodpay.gateway.client.ClientWebClient;
import br.com.postech.sevenfoodpay.gateway.order.OrderWebClient;
import br.com.postech.sevenfoodpay.infraestruture.entities.PaymentEntity;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PaymentService implements PaymentPort {

    private static final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    @Value("${mercado_pago_sample_access_token}")
    private String mercadoPagoAccessToken;

    private final ClientWebClient restClient;
    private final OrderWebClient orderWebClient;
    private final PaymentRepositoryPort paymentRepositoryPort;

    @Autowired
    public PaymentService(ClientWebClient restClient, OrderWebClient orderWebClient, PaymentRepositoryPort paymentRepositoryPort) {
        this.restClient = restClient;
        this.orderWebClient = orderWebClient;
        this.paymentRepositoryPort = paymentRepositoryPort;
    }

    @Override
    public Optional<PaymentResponse> processPayment(PaymentRequest paymentRequest) {
        try {

            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            PaymentClient paymentClient = new PaymentClient();
            PaymentCreateDTO paymentCreateRequest =
                    getPaymentPix(paymentRequest);

            PaymentCreateRequest paymentCreate = paymentCreateRequest.getPaymentCreateRequest();
            log.info("PaymentCreateRequest: {}", paymentCreate);
            Payment createdPayment = paymentClient.create(paymentCreate);

            String orderId = paymentCreateRequest.getOrderId();
            String clientId = paymentCreateRequest.getClientId();
            Optional<PaymentResponse> paymentResponse = paymentResponse(createdPayment, clientId, orderId);

            if (!paymentResponse.isPresent()) {
                throw new MercadoPagoException("Payment ntransactionDetails = nullot created");
            }

            log.info("Payment created: {}", paymentResponse.get());
            return paymentResponse;
        } catch (MPApiException apiException) {
            System.out.println(apiException.getApiResponse().getContent());
            throw new MercadoPagoException(apiException.getApiResponse().getContent());
        } catch (MPException exception) {
            System.out.println(exception.getMessage());
            throw new MercadoPagoException(exception.getMessage());
        }
    }

    private Optional<PaymentResponse> paymentResponse(Payment payment, String clientId, String orderId) {
        try {
            PaymentDomain paymentEntity = PaymentDomain.builder()
                    .clientId(clientId)
                    .paymentId(payment.getId())
                    .paymentStatus(payment.getStatus())
                    .paymentDetails(payment.getStatusDetail())
                    .paymentDate(LocalDateTime.now())
                    .paymentAmount(payment.getTransactionAmount())
                    .qrCode(payment.getPointOfInteraction().getTransactionData().getQrCode())
                    .qrCodeBase64(payment.getPointOfInteraction().getTransactionData().getQrCodeBase64())
                    .orderId(orderId)
                    .build();

            paymentRepositoryPort.save(paymentEntity);

            return Optional.of(new PaymentResponse(
                    payment.getId(),
                    String.valueOf(payment.getStatus()),
                    payment.getStatusDetail(),
                    payment.getPointOfInteraction().getTransactionData().getQrCodeBase64(),
                    payment.getPointOfInteraction().getTransactionData().getQrCode()));
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
        }
        return null;
    }

    private PaymentCreateDTO getPaymentPix(PaymentRequest paymentRequest) {
        String clientId = paymentRequest.clientId();

        ClientResponse clientData = getClientData(clientId);
        log.info("Data: {}", clientData);

        String orderId = paymentRequest.orderId();
        OrderResponse orderData = getOrderData(orderId);
        log.info("Order: {}", orderData);

        BigDecimal totalPrice = orderData.getTotalPrice();
        log.info("Total Price: {}", totalPrice);

        PayerDTO payer = getPayerDTO(clientData);
        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(totalPrice)
                .description("Payment for order " + paymentRequest.orderId())
                .paymentMethodId(PaymentMethod.PIX.getMethod())
                .payer(
                        getPayer(payer))
                .build();

        return PaymentCreateDTO.builder()
                .paymentCreateRequest(paymentCreateRequest)
                .orderId(paymentRequest.orderId())
                .clientId(clientId)
                .build();
    }

    private PayerDTO getPayerDTO(ClientResponse clientData) {
        if (clientData == null) {
            throw new MercadoPagoException("Client not found");
        }

        PayerIdentificationDTO identificationDTO = new PayerIdentificationDTO();
        identificationDTO.setType("CPF");
        identificationDTO.setNumber(clientData.cpf().replaceAll("[^0-9]", ""));

        PayerDTO payer = new PayerDTO();
        payer.setEmail(clientData.email());

        String name = clientData.name();
        String[] names = name.split(" ");
        var firstName = names[0];
        var middleName = names.length > 2 ? names[1] : "";

        payer.setFirstName(firstName + " " + middleName);
        payer.setLastName(names[names.length - 1]);

        payer.setIdentification(identificationDTO);
        return payer;
    }

    private PaymentPayerRequest getPayer(PayerDTO payer) {
        PayerIdentificationDTO identification = payer.getIdentification();
        return PaymentPayerRequest.builder()
                .email(payer.getEmail())
                .firstName(payer.getFirstName())
                .lastName(payer.getLastName())
                .identification(
                        getIdentification(identification))
                .build();
    }

    private IdentificationRequest getIdentification(PayerIdentificationDTO identification) {
        return IdentificationRequest.builder()
                .type(identification.getType())
                .number(identification.getNumber())
                .build();
    }

    private ClientResponse getClientData(String clientId) {
        return restClient.getClientByCode(clientId);
    }

    private OrderResponse getOrderData(String orderId) {
        return orderWebClient.getOrderById(orderId);
    }
}
