package com.mercadopago.sample.dto.request;

import jakarta.validation.constraints.NotNull;

public record PayerRequest(@NotNull String firstName, @NotNull String lastName, @NotNull String email,
                           @NotNull PayerIdentificationRequest identification) {}
