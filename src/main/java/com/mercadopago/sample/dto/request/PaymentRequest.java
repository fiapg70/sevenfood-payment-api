package com.mercadopago.sample.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record PaymentRequest(@NotNull(message = "Amount should not be null") BigDecimal transactionAmount, @NotNull(message = "description should not be null") @JsonProperty("description")
String productDescription, @NotNull(message = "ClientId should not be null") String clientId, @NotNull(message = "Orders should not be null") @JsonProperty("orders") List<OrderDTO> orderDTOList) {}