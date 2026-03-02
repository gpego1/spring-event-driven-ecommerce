## 📌 Visão Geral

Este projeto faz parte de um sistema baseado em **Arquitetura Orientada a Eventos (Event-Driven Architecture)**.
O objetivo principal deste serviço é **publicar eventos no Apache Kafka** sempre que uma ação relevante ocorre no sistema principal (por exemplo, criação de pedidos em um e-commerce).

Neste contexto, este serviço atua como um **Producer Kafka**, enviando mensagens para um tópico específico que será consumido por outros serviços do ecossistema (como notificações, faturamento, analytics, etc.).

---

## 🧠 Conceito da Arquitetura

Em vez de serviços se comunicarem diretamente entre si (acoplamento forte), utilizamos eventos:

1. Um evento acontece no sistema (ex.: pedido criado).
2. O serviço produtor publica esse evento no Kafka.
3. Outros serviços interessados escutam o tópico e reagem ao evento.

Isso traz vantagens como:

* Baixo acoplamento entre serviços
* Alta escalabilidade
* Maior resiliência
* Comunicação assíncrona

---

## 🏗️ Papel deste Serviço no Projeto

Este serviço é responsável por:

* Receber ou gerar eventos do sistema
* Serializar os dados do evento
* Publicar mensagens em um **tópico Kafka**
* Garantir que outros microsserviços possam processar essas informações

Exemplo de evento enviado:

```json
{
  "orderId": "12345",
  "customerEmail": "cliente@email.com",
  "totalAmount": 299.90,
  "createdAt": "2026-03-01T10:30:00"
}
```

Esse evento pode ser consumido por:

* Serviço de notificações
* Serviço de pagamento
* Serviço de analytics
* Serviço de envio de e-mails

---

## ⚙️ Tecnologias Utilizadas

* Java
* Spring Boot
* Spring Kafka
* Apache Kafka
* Docker
* Docker Compose

---

## 🔄 Fluxo do Evento

1. Um pedido é criado no sistema principal (ecommerce).
2. O serviço produtor cria um objeto `OrderCreatedEvent`.
3. O producer envia esse evento para o Kafka.
4. O Kafka armazena o evento no tópico `order.created`.
5. Outros microsserviços consomem o evento.

Fluxo simplificado:

```
Ecommerce Service
       ↓
Notification Service (Producer)
       ↓
      Kafka
       ↓
Outros Microsserviços (Consumers)
```

---

## 🚀 Como Executar o Projeto

### 1. Subir a infraestrutura com Docker

No diretório do projeto principal:

```bash
docker compose up -d
```

Isso irá iniciar:

* Kafka
* Zookeeper (se configurado)
* Banco de dados
* Outros serviços necessários

---

### 2. Executar o serviço

Se estiver rodando localmente:

```bash
./mvnw spring-boot:run
```

Ou com Maven:

```bash
mvn spring-boot:run
```

---

## 🔧 Configuração do Kafka

Exemplo de configuração no `application.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: kafka:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

Tópico utilizado:

```
order.created
```

---

## 📤 Exemplo de Producer

Exemplo simplificado de envio de evento:

```java
kafkaTemplate.send("order.created", orderEvent);
```

Esse comando publica a mensagem no Kafka para que outros serviços possam consumi-la.

---

## 🧪 Como Testar se o Evento Foi Enviado

Você pode verificar se o evento chegou ao Kafka executando:

```bash
docker exec -it kafka /bin/bash
```

Depois:

```bash
kafka-console-consumer.sh \
--topic order.created \
--bootstrap-server localhost:9092 \
--from-beginning
```

Se estiver funcionando corretamente, os eventos aparecerão no terminal.

---

## 📈 Benefícios Dessa Abordagem

Este modelo traz diversas vantagens para o sistema:

* Comunicação desacoplada entre serviços
* Escalabilidade horizontal
* Facilidade para adicionar novos serviços consumidores
* Processamento assíncrono
* Melhor tolerância a falhas

---

## 🎯 Objetivo no Projeto de Portfólio

Este serviço foi desenvolvido para demonstrar conhecimento em:

* Microsserviços
* Arquitetura orientada a eventos
* Integração com Kafka
* Comunicação assíncrona
* Docker e ambientes distribuídos

Ele faz parte de um sistema maior de **e-commerce baseado em eventos**, onde cada serviço tem uma responsabilidade específica.
