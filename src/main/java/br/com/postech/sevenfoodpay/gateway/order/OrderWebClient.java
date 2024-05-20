package br.com.postech.sevenfoodpay.gateway.order;

import br.com.postech.sevenfoodpay.gateway.dto.OrderResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OrderWebClient {

    private final WebClient webClient;

    public OrderWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9996/api/v1").build();
    }

    public OrderResponse getOrderById(String orderId) {
        return webClient.get()
                .uri("/orders/code/{orderId}", orderId)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();
    }
}