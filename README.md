# API de Processamento de Pedidos

## Descrição
Esta aplicação é uma API desenvolvida em **Java Spring Boot** que processa pedidos utilizando **Kafka** para mensageria e **PostgreSQL** como banco de dados. A aplicação possui endpoints para envio de pedidos e consulta de pedidos finalizados. O fluxo inclui a atualização de status dos pedidos durante o processamento, garantindo um pipeline de dados eficiente e escalável.

## Tecnologias Utilizadas

- **Java Spring Boot**: Framework principal para a construção da aplicação.
- **Apache Kafka**: Utilizado como sistema de mensageria para comunicação entre o producer e o consumer.
- **PostgreSQL**: Banco de dados para armazenamento dos pedidos processados.
- **Postman**: Ferramenta utilizada para testes dos endpoints.

## Funcionalidades

### Endpoints Disponíveis

#### 1. Enviar Pedido
- **URL**: `/pedidos`
- **Método**: `POST`
- **Descrição**: Recebe um pedido, publica no tópico Kafka e define o status como "Em processamento".
- **Exemplo de Requisição**:
  ```json
  {
  "cliente": "Cliente coca",
  "status": "Em andamento",
  "produtos": [
    {
      "nome": "Produto A",
      "quantidade": 2,
      "preco": 50.00
    },
    {
      "nome": "Produto B",
      "quantidade": 1,
      "preco": 30.00
    }
  ]
  }

  ```
- **Exemplo de Resposta**:
  ```json
  {
    "mensagem": "Pedido enviado com sucesso!",
    "status": "Em processamento"
  }
  ```

#### 2. Consultar Pedidos Finalizados
- **URL**: `/pedidos?status=Finalizado`
- **Método**: `GET`
- **Descrição**: Retorna uma lista de pedidos que possuem o status "Finalizado".
- **Exemplo de Resposta**:
  ```json
  [
    {
      "id": "123",
      "descricao": "Pedido de teste",
      "valor": 150.0,
      "status": "Finalizado"
    }
  ]
  ```

## Fluxo da Aplicação

1. **Recebimento do Pedido**:
   - Um pedido é enviado via o endpoint `/pedidos`.
   - O pedido é publicado em um tópico Kafka e seu status é definido como "Em processamento".

2. **Processamento do Pedido**:
   - O consumer lê o tópico Kafka.
   - Realiza os cálculos necessários no pedido.
   - Grava o pedido processado no banco de dados PostgreSQL.
   - Atualiza o status do pedido para "Finalizado".

3. **Consulta de Pedidos Finalizados**:
   - O endpoint `/pedidos?status=Finalizado` permite a consulta apenas dos pedidos com status "Finalizado", retornando os dados processados.

## Arquitetura

### Diagrama do Fluxo
```plaintext
[Cliente] --> [API - Endpoint /pedidos] --> [Producer - Kafka Topic]
                                                 |
                                          [Consumer - Kafka Topic]
                                                 |
                                        [Banco de Dados PostgreSQL]
                                                 |
                      [API - Endpoint /pedidos?status=Finalizado]
                                                 |
                                             [Cliente]
```

### Estrutura de Código
- **Controller**: Gerencia as requisições HTTP.
- **Service**: Contém a lógica de negócios para envio e processamento dos pedidos.
- **Repository**: Interface para comunicação com o banco de dados PostgreSQL.
- **Config**: Configurações de Kafka para producer e consumer.

## Pré-requisitos

1. **Java 17**
2. **Apache Kafka** configurado e em execução.
3. **PostgreSQL** configurado e em execução.
4. **Postman** (opcional, para testes manuais).

## Como Executar

1. Clone o repositório:
   ```bash
   git clone <URL-do-repositorio>
   cd <nome-do-projeto>
   ```

2. Configure o **application.properties** ou **application.yml** com as informações do banco de dados e do Kafka:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha

   spring.kafka.bootstrap-servers=localhost:9092
   spring.kafka.consumer.group-id=grupo_consumer
   spring.kafka.topic.pedidos=nome_do_topico
   ```

3. Compile e execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Teste os endpoints usando o Postman ou outra ferramenta de sua escolha.

## Testes

### Testes Locais
- Use o Postman para testar o envio de pedidos e a consulta de pedidos finalizados.

### Testes de Integração
- Certifique-se de que o Kafka e o PostgreSQL estejam configurados corretamente antes de executar os testes.

## Observações
- Certifique-se de monitorar os logs da aplicação para identificar possíveis falhas durante o processamento dos pedidos.
- Configure alertas no Kafka ou em ferramentas de observabilidade para identificar falhas de consumo ou problemas de backlog.

---

