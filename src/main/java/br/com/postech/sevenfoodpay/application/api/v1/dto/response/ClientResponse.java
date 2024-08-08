package br.com.postech.sevenfoodpay.application.api.v1.dto.response;

public record ClientResponse(String id, String name, String email, String socialId, String code, String description) {}
