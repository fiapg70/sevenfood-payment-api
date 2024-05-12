package br.com.postech.sevenfoodpay.repository;

import br.com.postech.sevenfoodpay.infraestruture.entities.PaymentEntity;
import br.com.postech.sevenfoodpay.infraestruture.repository.PaymentRepository;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.collections4.iterators.EmptyIterator;
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

import javax.imageio.metadata.IIOInvalidTreeException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
    public void should_find_no_payments_if_repository_is_not_empty() {
        Iterable<PaymentEntity> paymentEntities = paymentRepository.findAll();
        paymentEntities.iterator();
        Assert.assertNotNull(paymentEntities);
    }

    @Test
    public void should_find_no_payments_if_repository_is_empty() {
        paymentRepository.deleteAll();
        Iterable<PaymentEntity> paymentEntities = paymentRepository.findAll();
        List<PaymentEntity> paymentEntityList = new ArrayList<>();
        paymentEntities.forEach(paymentEntityList::add);
        Assert.assertEquals(0, paymentEntityList.size());
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

        assertThat(client).hasFieldOrPropertyWithValue("paymentStatus", "PENDING");
        assertThat(client).hasFieldOrPropertyWithValue("clientId", "1");
    }

    @Test
    public void should_find_by_id_no_payments_if_repository_contains_data() {
        Optional<PaymentEntity> paymentEntities = paymentRepository.findById(1l);
        Assert.assertNotNull(paymentEntities.get());
    }

    @Test
    public void whenDerivedExceptionThrown_thenAssertionSucceeds() {
        assertThrows(ConstraintViolationException.class, () -> {
            PaymentEntity paymentEntity = getPayment();
            paymentEntity.setClientId(null);
            entityManager.persist(paymentEntity);
            entityManager.flush();
        });
    }

}