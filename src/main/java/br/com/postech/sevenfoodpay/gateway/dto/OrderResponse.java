package br.com.postech.sevenfoodpay.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private String clientId;
    private String orderId;
    private String statusPedido;
    private BigDecimal totalPrice;
}