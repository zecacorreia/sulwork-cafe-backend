# ☕ Sulwork Café - Backend

Esta é a API RESTful para a aplicação Sulwork Café. Desenvolvida com Spring Boot, ela é responsável por toda a lógica de negócio, gerenciamento de dados e comunicação com o banco de dados PostgreSQL.

---

## 🛠️ Tech Stack

* **Java 21** e **Spring Boot 3**
* **Maven** para gerenciamento de dependências
* **PostgreSQL** como banco de dados
* **Docker** & **Docker Compose** para o ambiente de desenvolvimento
* **Spring Data JPA (Hibernate)** para persistência de dados
* **Flyway** para migrações de banco de dados
* **SpringDoc (OpenAPI v3)** para documentação automática da API via Swagger UI

---

##  Prerequisites Pré-requisitos

Para executar o backend, você precisará ter instalado:

* [Git](https://git-scm.com/)
* [Docker](https://www.docker.com/products/docker-desktop/) e Docker Compose
* [Java JDK 21](https://www.oracle.com/java/technologies/downloads/#jdk21-windows) (opcional, se for rodar fora do Docker)

---

## 🚀 Como Rodar o Projeto

O ambiente de desenvolvimento do backend é 100% containerizado com Docker Compose, simplificando a configuração.

1.  **Clone o Repositório**
    ```bash
    git clone https://github.com/zecacorreia/sulwork-cafe-backend
    cd sulwork-cafe-backend
    ```

2.  **Arquivo de Ambiente (`.env`)**
    Este projeto usa um arquivo `.env` para as credenciais do banco. Crie um arquivo chamado `.env` na raiz do projeto backend com o seguinte conteúdo. Estes são os valores padrão que o `docker-compose.yml` espera.
    ```
    SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/sulwork_cafe
    SPRING_DATASOURCE_USERNAME=sulwork
    SPRING_DATASOURCE_PASSWORD=sulwork
    ```

3.  **Inicie os Containers**
    Execute o seguinte comando para construir a imagem da aplicação e iniciar os containers do backend e do banco de dados em segundo plano (`-d`).
    ```bash
    docker-compose up --build -d
    ```

4.  **Verificação**
    * Para confirmar que os containers estão rodando, execute `docker ps`. Você deve ver `sulwork_cafe_app` e `sulwork_cafe_db` com o status "Up".
    * A API estará disponível em `http://localhost:8080`.

---

## 📖 Documentação da API

Com o backend rodando, a documentação interativa da API (gerada pelo SpringDoc) pode ser acessada através do Swagger UI no seguinte endereço:

[http://localhost:8080/swagger](http://localhost:8080/swagger)
