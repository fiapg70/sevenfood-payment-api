package com.mercadopago.sample.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.sample.api.v1.PaymentResource;
import com.mercadopago.sample.dto.*;
import com.mercadopago.sample.dto.request.PaymentRequest;
import com.mercadopago.sample.dto.response.ClientResponse;
import com.mercadopago.sample.dto.response.PaymentResponse;
import com.mercadopago.sample.exception.MercadoPagoException;
import com.mercadopago.sample.model.entities.PaymentEntity;
import com.mercadopago.sample.model.repositories.PaymentRepository;
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
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    @Value("${mercado_pago_sample_access_token}")
    private String mercadoPagoAccessToken;

    @Autowired
    private ClientWebClient restClient;

    @Autowired
    private OrderWebClient orderWebClient;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Optional<PaymentResponse> processPayment(PaymentRequest paymentRequest) {
        try {

            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            PaymentClient paymentClient = new PaymentClient();
            PaymentCreateDTO paymentCreateRequest =
                    getPaymentPix(paymentRequest);

            Payment createdPayment = paymentClient.create(paymentCreateRequest.getPaymentCreateRequest());

            String orders = paymentCreateRequest.getOrders();
            String clientId = paymentCreateRequest.getClientId();
            Optional<PaymentResponse> paymentResponse = paymentResponse(createdPayment, clientId, orders);

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

    private Optional<PaymentResponse> paymentResponse(Payment payment, String clientId, String orders) {

        PaymentEntity paymentEntity = PaymentEntity.builder()
                .clientId(clientId)
                .paymentId(payment.getId())
                .paymentStatus(payment.getStatus())
                .paymentDetails(payment.getStatusDetail())
                .paymentDate(LocalDateTime.now())
                .paymentAmount(payment.getTransactionAmount())
                .qrCode(payment.getPointOfInteraction().getTransactionData().getQrCode())
                .qrCodeBase64(payment.getPointOfInteraction().getTransactionData().getQrCodeBase64())
                .orders(orders)
                .build();

        paymentRepository.save(paymentEntity);

        return Optional.of(new PaymentResponse(
                payment.getId(),
                String.valueOf(payment.getStatus()),
                payment.getStatusDetail(),
                payment.getPointOfInteraction().getTransactionData().getQrCodeBase64(),
                payment.getPointOfInteraction().getTransactionData().getQrCode()));
    }

    private PaymentCreateDTO getPaymentPix(PaymentRequest paymentRequest) {
        String clientId = paymentRequest.clientId();
        ClientResponse clientData = getClientData(clientId);
        log.info("Data: {}", clientData);

        StringBuffer orders = new StringBuffer();
        PayerDTO payer = getPayerDTO(clientData);
        AtomicReference<BigDecimal> amountTotal = new AtomicReference<>(BigDecimal.ZERO);
        paymentRequest.orderDTOList().forEach(orderDTO -> {
            log.info("Order: {}", orderDTO.id());

            List<OrderDTO> order = orderWebClient.getOrderById(orderDTO.id());
            log.info("Order: {}", order);

            AtomicReference<BigDecimal> amountOrder = new AtomicReference<>(BigDecimal.ZERO);
            order.forEach(orderDTO1 -> {
                log.info("Order: {}", orderDTO1);
                BigDecimal amount = orderDTO1.getProducts().stream()
                        .map(product -> product.getPrice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                amountOrder.set(amountOrder.get().add(amount));
            });

            amountTotal.set(amountTotal.get().add(amountOrder.get()));
            orders.append(orderDTO.id() + ":");
        });

        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(amountTotal.get())
                .description(paymentRequest.productDescription())
                .paymentMethodId(PaymentMethod.PIX.getMethod())
                .payer(
                        getPayer(payer))
                .build();

        return PaymentCreateDTO.builder()
                .paymentCreateRequest(paymentCreateRequest)
                .orders(orders.toString())
                .clientId(clientId)
                .build();
    }

    private static PayerDTO getPayerDTO(ClientResponse clientData) {
        if (clientData == null) {
            throw new MercadoPagoException("Client not found");
        }

        PayerIdentificationDTO identificationDTO = new PayerIdentificationDTO();
        identificationDTO.setType(clientData.documentType());
        identificationDTO.setNumber(clientData.documentNumber().replaceAll("[^0-9]", ""));

        PayerDTO payer = new PayerDTO();
        payer.setEmail(clientData.email());
        payer.setFirstName(clientData.firstName());
        payer.setLastName(clientData.lastName());
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
        return restClient.getUserById(clientId);
    }
}
