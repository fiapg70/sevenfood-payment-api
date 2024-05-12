package br.com.postech.sevenfoodpay.service;

import br.com.postech.sevenfoodpay.application.database.mapper.PaymentMapper;
import br.com.postech.sevenfoodpay.core.domain.PaymentDomain;
import br.com.postech.sevenfoodpay.core.ports.out.PaymentRepositoryPort;
import br.com.postech.sevenfoodpay.core.service.PaymentService;
import br.com.postech.sevenfoodpay.infraestruture.entities.PaymentEntity;
import br.com.postech.sevenfoodpay.infraestruture.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Disabled
    public void getAllEmployeesTest() {
        List<PaymentEntity> listEntity = getPaymentEntities();
        //listEntity.add(clientEntity1);
        //listEntity.add(clientEntity2);

        when(paymentRepository.findAll()).thenReturn(listEntity);
        List<PaymentDomain> paymentDomains = mapper.map(listEntity);

        // test
        //List<PaymentDomain> restaurantList = paymentService.findAll();

        //assertNotNull(restaurantList);
        //verify(restaurantRepository, times(1)).findAll();
        assertNotNull(paymentDomains);
    }

    private List<PaymentEntity> getPaymentEntities() {
        List<PaymentDomain> list = new ArrayList<>();
        List<PaymentEntity> listEntity = new ArrayList<>();

        PaymentDomain paymentDomain = getPaymentoDomain();
        // PaymentoDomain client1 = getPaymentoDomain();
        //PaymentoDomain client2 = getPaymentoDomain();

        PaymentEntity clientEntity = getPaymentEntity();
        //PaymentoDomainEntity clientEntity1 = getPaymentoDomainEntity();
        //PaymentoDomainEntity clientEntity2 = getPaymentoDomainEntity();

        list.add(paymentDomain);
        //list.add(client1);
        //list.add(client2);

        listEntity.add(clientEntity);
        return listEntity;
    }

}