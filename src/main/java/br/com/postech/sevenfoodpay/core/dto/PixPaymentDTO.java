package br.com.postech.sevenfoodpay.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PixPaymentDTO {
    @NotNull
    private BigDecimal transactionAmount;

    @NotNull
    @JsonProperty("description")
    private String productDescription;

    @NotNull
    private PayerDTO payer;

    public PixPaymentDTO() {
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public PayerDTO getPayer() {
        return payer;
    }

    public void setPayer(PayerDTO payer) {
        this.payer = payer;
    }
}
