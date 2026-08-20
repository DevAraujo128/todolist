# Todo List API 📋

API REST para cadastro de usuários e gerenciamento de tarefas. O projeto foi desenvolvido com Spring Boot e utiliza autenticação HTTP Basic nas rotas de tarefas.

## Tecnologias

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Banco H2 em memória
- BCrypt para hash de senhas
- Maven
- Docker

## Como executar 🚀

### Pré-requisitos

- JDK 17 ou superior
- Maven 3.9 ou superior, ou o Maven Wrapper incluído no projeto

### Opção 1: direto na máquina

Escolha esta opção se o JDK estiver instalado. No Windows, execute:

```bash
./mvnw.cmd spring-boot:run
```

No Linux/macOS, execute:

```bash
./mvnw spring-boot:run
```

A API será iniciada em `http://localhost:8080`. No Linux/macOS, use `./mvnw spring-boot:run`.

### Opção 2: com Docker

Escolha esta opção se preferir executar a aplicação dentro de um container. Não é necessário instalar o Maven na máquina para este fluxo.

```bash
docker build -t todolist .
docker run --rm -p 8080:8080 todolist
```

Com o container em execução, a API também estará disponível em `http://localhost:8080`.

## Testes 🧪

Para executar os testes automatizados:

```bash
./mvnw.cmd test
```

O teste atual verifica se o contexto da aplicação Spring inicia corretamente. Para Linux/macOS, use `./mvnw test`.

## Banco de dados local

O projeto usa o H2 em memória. Os dados são apagados quando a aplicação é encerrada.

Em execução local, o console do H2 fica disponível em:

`http://localhost:8080/h2-console`

Use os valores definidos em `src/main/resources/application.properties` para conexão. Atualmente, eles são:

| Campo    | Valor                                      |
| -------- | ------------------------------------------ |
| JDBC URL | `jdbc:h2:mem:todolist`                     |
| Usuário  | valor definido em `application.properties` |
| Senha    | valor definido em `application.properties` |

Os valores atuais são credenciais padrão para o banco H2 em memória durante o desenvolvimento. Não use essa configuração em produção, não publique credenciais reais no repositório e não deixe o console do H2 exposto em uma API pública.

## Autenticação 🔐

As rotas de `/tasks/` usam autenticação HTTP Basic. Primeiro crie um usuário em `/users/`; depois envie as credenciais escolhidas no parâmetro `-u` do `curl` ou no header `Authorization`.

> Apesar de a criação de tarefas ser o primeiro passo após o login, o filtro atual também exige autenticação no `POST /tasks/`.

## Rotas da API 🌐

### 1. Criar usuário

Não exige autenticação.

`POST https://todolist-rocket-kct5.onrender.com/users/`

```bash
curl -X POST "https://todolist-rocket-kct5.onrender.com/users/" -H "Content-Type: application/json" -d "{\"username\":\"seu_usuario\",\"name\":\"Seu nome\",\"password\":\"SUA_SENHA\"}"
```

Corpo da requisição:

```json
{
  "username": "seu_usuario",
  "name": "Seu nome",
  "password": "SUA_SENHA"
}
```

A senha é armazenada com hash BCrypt. O `username` deve ser único.

### 2. Criar tarefa

Exige autenticação Basic.

`POST https://todolist-rocket-kct5.onrender.com/tasks/`

```bash
curl -X POST "https://todolist-rocket-kct5.onrender.com/tasks/" -u "seu_usuario:SUA_SENHA" -H "Content-Type: application/json" -d "{\"title\":\"Estudar Java\",\"description\":\"Revisar Spring Boot\",\"priority\":\"Alta\",\"startAt\":\"2026-08-21T09:00:00\",\"endAt\":\"2026-08-21T10:00:00\"}"
```

Corpo da requisição:

```json
{
  "title": "Estudar Java",
  "description": "Revisar Spring Boot",
  "priority": "Alta",
  "startAt": "2026-08-21T09:00:00",
  "endAt": "2026-08-21T10:00:00"
}
```

As datas devem estar no formato `yyyy-MM-dd'T'HH:mm:ss`, ser posteriores ao momento atual e `startAt` deve ser anterior a `endAt`. O campo `title` aceita no máximo 50 caracteres.

### 3. Listar tarefas do usuário autenticado

Exige autenticação Basic e retorna apenas as tarefas do usuário autenticado.

`GET https://todolist-rocket-kct5.onrender.com/tasks/`

```bash
curl -X GET "https://todolist-rocket-kct5.onrender.com/tasks/" -u "seu_usuario:SUA_SENHA"
```

### 4. Atualizar parcialmente uma tarefa

Exige autenticação Basic e só permite alterar tarefas pertencentes ao usuário autenticado. Envie apenas os campos que deseja modificar.

`PUT https://todolist-rocket-kct5.onrender.com/tasks/{id}`

```bash
curl -X PUT "https://todolist-rocket-kct5.onrender.com/tasks/{id}" -u "seu_usuario:SUA_SENHA" -H "Content-Type: application/json" -d "{\"priority\":\"Baixa\",\"description\":\"Revisar Spring Boot e testes\"}"
```

## Fluxo recomendado

1. Crie um usuário com `POST /users/`.
2. Use as credenciais criadas na autenticação Basic.
3. Crie uma tarefa com `POST /tasks/`.
4. Liste suas tarefas com `GET /tasks/`.
5. Atualize uma tarefa com `PUT /tasks/{id}`.

## Tratamento de erros

- `200 OK`: operação concluída.
- `400 Bad Request`: usuário duplicado, datas inválidas, tarefa inexistente ou falta de permissão.
- `401 Unauthorized`: credenciais ausentes, usuário inexistente ou senha incorreta.

## Deploy 🐳

O projeto possui um `Dockerfile` preparado para compilar a aplicação com Maven e executá-la em uma imagem com Java 17. A aplicação escuta na porta `8080`.

O comando `docker build` executa `mvn clean install`, incluindo os testes automatizados. Assim, a validação pode ser feita diretamente na máquina com Maven ou durante a criação da imagem Docker.

O endereço publicado usado nos exemplos é `https://todolist-rocket-kct5.onrender.com`. Não inclua senhas, tokens ou dados de usuários reais em commits, issues ou exemplos públicos.
