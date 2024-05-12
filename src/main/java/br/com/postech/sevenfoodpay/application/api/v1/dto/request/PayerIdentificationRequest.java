package br.com.postech.sevenfoodpay.application.api.v1.dto.request;

import jakarta.validation.constraints.NotNull;

public record PayerIdentificationRequest(@NotNull String type, @NotNull String number, @NotNull String clientId) {}
