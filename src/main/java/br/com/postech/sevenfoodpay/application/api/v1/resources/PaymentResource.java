package br.com.postech.sevenfoodpay.application.api.v1.resources;

import br.com.postech.sevenfoodpay.application.api.v1.dto.request.PaymentRequest;
import br.com.postech.sevenfoodpay.application.api.v1.dto.response.PaymentResponse;
import br.com.postech.sevenfoodpay.application.api.v1.mapper.PaymentApiMapper;
import br.com.postech.sevenfoodpay.commons.util.RestUtils;
import br.com.postech.sevenfoodpay.core.domain.PaymentDomain;
import br.com.postech.sevenfoodpay.core.ports.in.PaymentPort;
import br.com.postech.sevenfoodpay.core.ports.out.PaymentRepositoryPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/v1/payments")
public class PaymentResource {

    private static final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private final PaymentPort paymentService;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final PaymentApiMapper paymentApiMapper;

    public PaymentResource(PaymentPort paymentService, PaymentRepositoryPort paymentRepositoryPort, PaymentApiMapper paymentApiMapper) {
        this.paymentService = paymentService;
        this.paymentRepositoryPort = paymentRepositoryPort;
        this.paymentApiMapper = paymentApiMapper;
    }

    @Operation(summary = "Create a payment by Order", tags = {"payment", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PaymentResponse> save(@Valid @RequestBody PaymentRequest request) {
        log.info("Objeto Recebido na requisição: {}", request);
        Optional<PaymentResponse> paymentResponse = paymentService.processPayment(request);
        if (paymentResponse.isPresent()) {
            URI location = RestUtils.getUri(paymentResponse.get().id());
            return ResponseEntity.created(location).body(paymentResponse.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Retrieve a Order by Id",
            description = "Get a Order object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"orders", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PaymentResponse> findOne(@PathVariable("id") Long id) {
        PaymentDomain orderSaved = paymentRepositoryPort.findById(id);
        if (orderSaved != null) {
            PaymentResponse orderResponse = paymentApiMapper.toPaymentResponse(orderSaved);
            return ResponseEntity.ok(orderResponse);
        }

        return ResponseEntity.noContent().build();
    }
}
