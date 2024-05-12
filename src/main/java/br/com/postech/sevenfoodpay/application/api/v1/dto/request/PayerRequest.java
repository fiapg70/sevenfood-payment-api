package br.com.postech.sevenfoodpay.application.api.v1.dto.request;

import jakarta.validation.constraints.NotNull;

public record PayerRequest(@NotNull String firstName, @NotNull String lastName, @NotNull String email,
                           @NotNull PayerIdentificationRequest identification) {}
