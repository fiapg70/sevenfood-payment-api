package br.com.postech.sevenfoodpay.gateway.order;

import br.com.postech.sevenfoodpay.core.dto.OrderDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class OrderWebClient {

    private final WebClient webClient;

    public OrderWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:5000/").build();
    }

    public List<OrderDTO> getOrderById(String orderId) {
        return List.of(webClient.get()
                .uri("orders?code={orderId}", orderId)
                .retrieve()
                .bodyToMono(OrderDTO[].class)
                .block());
    }
}

//http://localhost:4000/orders?code=ORD-002