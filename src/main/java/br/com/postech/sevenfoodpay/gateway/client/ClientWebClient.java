package br.com.postech.sevenfoodpay.gateway.client;

import br.com.postech.sevenfoodpay.application.api.v1.dto.response.ClientResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ClientWebClient {
    private final WebClient webClient;

    public ClientWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9992/api/v1").build();
    }

    public ClientResponse getClientByCode(String clientId) {
        return webClient.get()
                .uri("/clients/code/{clientId}", clientId)
                .retrieve()
                .bodyToMono(ClientResponse.class)
                .block();
    }
}