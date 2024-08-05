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

/// TODO - AJUSTE 
# Spring Boot 3 API

Este projeto é uma API desenvolvida em Spring Boot 3. Este README fornece instruções para desenvolvedores que desejam rodar o projeto localmente, incluindo como rodar o JAR usando Maven, configurar o ambiente no IntelliJ IDEA, utilizar arquivos `.env` para gerenciar variáveis de ambiente e como rodar o PostgreSQL e o SonarQube usando Docker Compose.

## Pré-requisitos

- Java 17+
- Maven 3.6+
- Docker e Docker Compose
- IntelliJ IDEA

## Rodando o JAR usando Maven

1. Clone o repositório:
    ```bash
    git clone https://github.com/fiapg70/sevenfood-client-api
    cd sevenfood-client-api
    ```

2. Compile e rode o JAR:
    ```bash
    mvn clean install
    java -jar target/nome-do-jar.jar
    ```

## Configurando o Ambiente no IntelliJ IDEA

1. Abra o IntelliJ IDEA e selecione `File -> Open...` e escolha o diretório do projeto.

2. Configure as variáveis de ambiente:
   - Clique com o botão direito no projeto no painel lateral e selecione `Edit Configurations...`.
   - Clique no ícone `+` no canto superior esquerdo e selecione `Application`.
   - Configure os campos:
      - **Name**: Nome do projeto
      - **Main class**: `br.com.sevenfood.client.sevenfoodclientapi.RunApplication` (substitua `br.com.sevenfood.client.sevenfoodclientapi.RunApplication` pela sua classe principal)
      - **VM options**: `-Dspring.profiles.active=prod` (Configuração para perfil de produção)
      - **Environment variables**: Clique no ícone `...` e adicione as variáveis necessárias:
        ```properties
        API_PORT=9999
        AWS_ACCESS_KEY_ID=<<Valor>>
        AWS_REGION=<<Valor>>
        AWS_SECRET_ACCESS_KEY=<<Valor>>
        AWS_SQS_QUEUE_ARN=<<Valor>>
        AWS_SQS_QUEUE_NAME=<<Valor>>
        AWS_SQS_QUEUE_URL=<<Valor>>
        DATABASE_PASSWORD=<<Valor>>
        DATABASE_URL=jdbc:postgresql://localhost:5432/<<DatabaseValor>>
        DATABASE_USERNAME=<<Valor>>
        MAIL_HOST=<<Valor>>
        MAIL_PASSWORD=<<Valor>>
        MAIL_PORT=<<Valor>>
        MAIL_USERNAME=<<Valor>>
        SECURITY_JWT_SECRET_KEY=<<Valor>>
        SES_SMTP_PASSWORD=<<Valor>>
        SES_SMTP_USERNAME=<<Valor>>
        SNS_TOPIC_EMAIL_ARN=<<Valor>>
        API_GATEWAY_URL_PRIVACY_NOTIFICATION_STATUS=<<Valor>>
        ```

3. Rode a aplicação:
   - Clique no ícone `Run` no canto superior direito ou pressione `Shift + F10`.

## Utilizando Arquivos `.env`

Para gerenciar variáveis de ambiente usando arquivos `.env`, siga estas etapas:

1. Crie um arquivo `.env` na raiz do projeto:
    ```properties
    API_PORT=9999
    AWS_ACCESS_KEY_ID=<<Valor>>
    AWS_REGION=<<Valor>>
    AWS_SECRET_ACCESS_KEY=<<Valor>>
    AWS_SQS_QUEUE_ARN=<<Valor>>
    AWS_SQS_QUEUE_NAME=<<Valor>>
    AWS_SQS_QUEUE_URL=<<Valor>>
    DATABASE_PASSWORD=<<Valor>>
    DATABASE_URL=jdbc:postgresql://localhost:5432/<<DatabaseValor>>
    DATABASE_USERNAME=<<Valor>>
    MAIL_HOST=<<Valor>>
    MAIL_PASSWORD=<<Valor>>
    MAIL_PORT=<<Valor>>
    MAIL_USERNAME=<<Valor>>
    SECURITY_JWT_SECRET_KEY=<<Valor>>
    SES_SMTP_PASSWORD=<<Valor>>
    SES_SMTP_USERNAME=<<Valor>>
    SNS_TOPIC_EMAIL_ARN=<<Valor>>
    API_GATEWAY_URL_PRIVACY_NOTIFICATION_STATUS=<<Valor>>
    ```

2. Adicione a dependência `spring-boot-dotenv` ao seu `pom.xml`:
    ```xml
    <dependency>
        <groupId>io.github.cdimascio</groupId>
        <artifactId>java-dotenv</artifactId>
        <version>5.2.2</version>
    </dependency>
    ```

3. Configure sua aplicação para ler o arquivo `.env`. Adicione o seguinte código à classe principal ou a uma configuração:
    ```java
    import io.github.cdimascio.dotenv.Dotenv;

    @SpringBootApplication
    public class RunApplication {
        public static void main(String[] args) {
            Dotenv dotenv = Dotenv.configure().load();
            System.setProperty("DB_URL", dotenv.get("DB_URL"));
            System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
            System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
            SpringApplication.run(RunApplication.class, args);
        }
    }
    ```

4. Configure as propriedades do Spring Boot para usar as variáveis de ambiente:
    ```properties
    spring.datasource.url=${DB_URL}
    spring.datasource.username=${DB_USERNAME}
    spring.datasource.password=${DB_PASSWORD}
    ```

## Rodando PostgreSQL e SonarQube com Docker Compose

O projeto inclui um arquivo `docker-compose.yml` no pacote `infra/` para rodar o PostgreSQL e o SonarQube. Siga as instruções abaixo para configurar e rodar esses serviços:

1. Navegue até o diretório `infra`:
    ```bash
    cd infra/postgres
    # ou
    cd infra/sonar
    ```

2. Rode o Docker Compose:
    ```bash
    docker-compose up -d
    ```

3. Verifique se os serviços estão rodando:
    ```bash
    docker-compose ps
    ```

O PostgreSQL estará disponível na porta 5432 e o SonarQube na porta 9000.

## Configurando o Perfil de Produção

Para rodar o perfil de produção, adicione a opção `-Dspring.profiles.active=prod` ao rodar o Maven ou configurar no IntelliJ IDEA.

### Rodando com Maven

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
Ambientes: prod,hom,dev
