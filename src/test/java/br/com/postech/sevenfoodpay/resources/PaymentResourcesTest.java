package br.com.postech.sevenfoodpay.resources;

import br.com.postech.sevenfoodpay.core.domain.PaymentDomain;
import br.com.postech.sevenfoodpay.core.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import br.com.postech.sevenfoodpay.util.JsonUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@TestPropertySource("classpath:application-test.properties")
public class PaymentResourcesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PaymentService service;

    private PaymentDomain getPayment() {
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

    @Test
    void findsTaskById() throws Exception {
        Long id = 1l;

        mockMvc.perform(get("/v1/payments/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana Furtado Correia"));
    }

    @Disabled
    public void getAll() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/payments")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").exists());
    }


    @Disabled
    public void create() throws Exception {
        String create = JsonUtil.getJson(getPayment());

        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/payments")
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }


    @Disabled
    public void update() throws Exception {
        String update = JsonUtil.getJson(getPayment());

        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/payments/{id}", 1)
                        .content(update)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ana Furtado Correia"));
    }

    @Test
    public void delete() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders.delete("/v1/payments/{id}", 1) )
                .andExpect(status().isOk());
    }
}
