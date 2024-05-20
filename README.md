# Sevenfood Order API

Essa API serve para rebecer pedidos com a os order e o valor da order para processar o pagamento.

## Stack Utilizada

- Java 17
- Spring Boot 3
- Flyway
- IntelliJ IDEA
- PostgreSQL 12 (PGAdmin)
- Docker & Docker Compose
- Nginx como reverse proxy
- Swagger (OpenAPI)
- JUnit 5
- Mockito
- Maven
- Kubernetes (EKS)

## Funcionalidades

2. **Pagamento**: Recebe o Pagamento via REST e envia ao mercado pago recebendo o codigo em base 64 do pix.
4. **Controle de Status**: Gerenciamento do status do pagamento via REST

## Utilização do PostgreSQL

Foi utilizado o PostgreSQL para armazenar os dados. A infraestrutura está contida no repositório: [infra-rds-postgresql](https://github.com/fiapg70/infra-rds-postgresql).

## Como Rodar a API

### Pré-requisitos

- Java 17
- Docker & Docker Compose
- Maven
- PostgreSQL 12

### Passo a Passo

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/fiapg70/sevenfood-payment-api.git
   cd sevenfood-payment-api

### Banco de Dados:
Para rodar o Banco local só rodar o docker compose da pasta postgres, com o comando 
```bash
  docker-compose up -d
Para rodar na AWS, rodar a infra: https://github.com/fiapg70/infra-rds-postgresql

### Antes de Rodar a Aplicação:
As variaves necessárias para rodar a aplicação estão na pasta env

### Rodando a Aplicação (Nessa API é necessário passar o TOKEN do Mercado PAGO:
```bash
  mvn clean install
  mvn spring-boot:run -Dspring-boot.run.arguments="--mercado_pago_sample_access_token=YOUR_ACCESS_TOKEN"

### Acesso à Documentação:
  Acesse a documentação da API via Swagger em http://localhost:8888/swagger-ui.html.

:test_tube: Testes
Nas nossas instruções de teste você encontrará um guia sobre como criar usuários de teste.
Você deve criar uma chave Pix na conta do vendedor no Mercado Pago (lembre-se de que você pode criar e usar um usuário de teste como vendedor).
IMPORTANTE: há algumas limitações ao testar este método de pagamento com usuários de teste. Ao criar um pagamento, ele ficará pendente e o código PIX e o código QR correspondentes serão retornados, mas não será possível usar esses códigos para finalizar o fluxo e aprovar o pagamento de teste.


