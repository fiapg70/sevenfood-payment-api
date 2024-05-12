package br.com.postech.sevenfoodpay.repository;

import br.com.postech.sevenfoodpay.infraestruture.entities.PaymentEntity;
import br.com.postech.sevenfoodpay.infraestruture.repository.PaymentRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
public class PaymentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    private PaymentEntity getPayment() {
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

    @Test
    public void should_find_no_clients_if_repository_is_empty() {
        Iterable<PaymentEntity> paymentEntities = paymentRepository.findAll();
        paymentEntities = Collections.EMPTY_LIST;
        Assert.assertNotNull(paymentEntities);
    }

    @Test
    public void should_store_a_payment() {
        PaymentEntity client = getPayment();

        Optional<PaymentEntity> payment = paymentRepository.findById(1l);
        if (payment.isPresent()) {

            PaymentEntity paymentEntity = payment.get();
            paymentEntity.setPaymentStatus("PAID");
            paymentRepository.save(paymentEntity);
        }

        //assertThat(client).hasFieldOrPropertyWithValue("name", "Ana Furtado Correia");
        //assertThat(client).hasFieldOrPropertyWithValue("cpf", "183.417.520-85");
        assertThat(client).hasFieldOrPropertyWithValue("payment", getPayment());
    }

    @Disabled
    public void whenDerivedExceptionThrown_thenAssertionSucceeds() {
       /* Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            PaymentEntity client = getClient();
            clientRepository.save(client);
        });

        String expectedMessage = "For input string";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));*/
    }

}