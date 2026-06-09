# URL Shortener

Um encurtador de URLs desenvolvido em **Java 21** e **Spring Boot**, construído de forma incremental com foco em aprendizado contínuo e aplicação de conceitos utilizados em ambientes reais.

O projeto começou como um simples MVP e vem evoluindo progressivamente para uma arquitetura mais robusta, incorporando persistência de dados, autenticação, cache, mensageria e, futuramente, observabilidade, CI/CD e deploy em nuvem.

---

## Objetivos do Projeto

* Praticar desenvolvimento backend com Java e Spring Boot.
* Aplicar boas práticas de arquitetura em camadas.
* Explorar tecnologias utilizadas pelo mercado.
* Evoluir gradualmente a aplicação, simulando um ambiente real.
* Construir um projeto sólido para portfólio.

---

## Arquitetura

O projeto segue uma arquitetura em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Princípios adotados:

* Injeção de dependência por construtor.
* Controllers trabalham apenas com DTOs.
* Uso de Optional<T> para retornos opcionais.
* Separação de responsabilidades.
* Evolução incremental por fases.
* Commits utilizando Conventional Commits.

---

# Tecnologias Utilizadas

### Linguagem e Framework

* Java 21
* Spring Boot 4.0.6
* Maven

### Persistência

* PostgreSQL 14
* Spring Data JPA
* Hibernate 7
* Flyway 11

### Segurança

* Spring Security 7
* JWT (jjwt 0.12.6)
* BCrypt

### Cache

* Redis
* Lettuce

### Mensageria

* RabbitMQ
* AMQP

### Testes

* JUnit 5
* Mockito
* AssertJ

### Utilitários

* Lombok

---

# Funcionalidades

## URLs

* Criar URL encurtada.
* Redirecionar para URL original.
* Persistência em PostgreSQL.

## Autenticação

* Cadastro de usuários.
* Login.
* Geração de JWT.
* Rotas públicas e protegidas.

## Cache

* Cache-Aside Pattern.
* Redis para otimização de consultas.
* TTL configurável.

## Mensageria

* Publicação de eventos de acesso.
* Processamento assíncrono utilizando RabbitMQ.
* Persistência dos acessos em banco.

---

# Evolução do Projeto

## Fase 1 — MVP

* Controller
* Service
* Domain
* Endpoint para criação de URLs
* Endpoint de redirecionamento

---

## Fase 2 — Persistência

* PostgreSQL
* JPA/Hibernate
* Flyway
* DTOs

### Migrations

* V1__create_urls_table.sql
* V2__create_users_table.sql
* V3__create_url_accesses_table.sql

---

## Fase 3 — Testes

Implementação dos testes unitários do UrlService.

Cenários cobertos:

* Geração de shortCode.
* Retorno de URL existente.
* Retorno vazio para URL inexistente.

---

## Fase 4 — Autenticação

Implementação de:

* Spring Security.
* JWT.
* Cadastro de usuários.
* Login.
* Filtro JWT.
* UserDetails.
* BCrypt.

Endpoints:

```http
POST /api/auth/register
POST /api/auth/login
```

---

## Fase 5 — Cache

Implementação do padrão Cache-Aside utilizando Redis.

Características:

* RedisTemplate<String, String>
* TTL configurável
* Recuperação prioritária do cache
* Consulta ao banco apenas quando necessário

---

## Fase 6 — Mensageria (Em desenvolvimento)

Implementação utilizando RabbitMQ.

Componentes:

* Queue
* Exchange
* Binding
* Producer
* Consumer
* Evento UrlAccessEvent

Objetivo:

Registrar os acessos de forma assíncrona, desacoplando a gravação do fluxo principal da aplicação.

---

# Estrutura do Projeto

```text
src
└── main
    ├── config
    ├── controller
    ├── dto
    ├── domain
    ├── repository
    ├── security
    ├── service
    └── messaging
```

---

# Configurações Importantes

## Flyway

A configuração do Flyway é realizada manualmente.

```properties
spring.flyway.enabled=false
spring.jpa.hibernate.ddl-auto=none
```

As migrations são executadas por meio da configuração personalizada.

---

## Variáveis de Ambiente

### Banco de Dados

```text
MINHASENHA
```

### JWT

```text
JWTSECRET
JWTEXPIRATION
```

---

# Próximas Evoluções

## Fase 6

* Finalizar mensageria.
* Validar persistência de acessos.

## Fase 7

Observabilidade:

* Spring Actuator
* Prometheus
* Grafana
* Logs estruturados

## Fase 8

Containerização:

* Docker
* Docker Compose

CI/CD:

* GitHub Actions

Deploy:

* Railway
* Render

---

# Melhorias Futuras

* Endpoint de deleção de URL com invalidação do cache Redis.
* Blacklist de tokens JWT para logout.
* Endpoint de estatísticas por URL.
* Swagger/OpenAPI.
* Tratamento global de exceções.
* Bean Validation.
* Dashboard de métricas.
* QR Code dinâmico.
* Domínios personalizados.

---

# Estratégia de Desenvolvimento

Cada funcionalidade é desenvolvida em uma branch específica:

```text
feature/fase-X-nome
```

Os commits seguem o padrão Conventional Commits:

```text
feat:
fix:
refactor:
test:
docs:
chore:
```

---

# Aprendizados Aplicados

Durante o desenvolvimento deste projeto foram aplicados conceitos de:

* APIs REST
* Arquitetura em camadas
* DTOs
* JPA e Hibernate
* Migrations com Flyway
* Testes unitários
* Autenticação com JWT
* Spring Security
* Cache com Redis
* Mensageria com RabbitMQ
* Programação assíncrona
* Boas práticas de projeto
* Clean Code

---

# Roadmap

* [x] MVP
* [x] Persistência
* [x] Testes
* [x] Autenticação
* [x] Cache
* [ ] Mensageria
* [ ] Observabilidade
* [ ] Docker
* [ ] CI/CD
* [ ] Deploy em Cloud

---

## Autor

Projeto desenvolvido por **Lucas Lowhan** como parte da jornada de estudos em Desenvolvimento Backend Java.
