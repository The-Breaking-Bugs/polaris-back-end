# Polaris Back-end

Um app de organização acadêmica que centraliza tarefas, lembretes e notas, com a visão futura de ser um assistente proativo de IA.

## 📚 Documentações

**[Feature: Modules](docs/features/modules.md)** - Documentação técnica da principal feature do projeto.

**[Feature: Tasks](docs/features/tasks.md)** - Documentação técnica da feature Tasks do projeto.

**[Feature: Notes](docs/features/notes.md)** - Documentação técnica da feature Notes do projeto.

---

## 🚀 Guia de Ambiente de Desenvolvimento

Siga os passos abaixo para configurar e rodar o projeto localmente.

### 1. Pré-requisitos

- **Java JDK 25:** Essencial para compilar e rodar o projeto.

  <details>
  <summary>Clique para ver as instruções de instalação do Java no Windows</summary>

  - **Baixe e instale o JDK 25:** [Link de Download da Oracle](https://www.oracle.com/java/technologies/downloads/#java25)
  - **Configure as variáveis de ambiente:**
    - Crie uma nova variável de sistema `JAVA_HOME` apontando para o diretório de instalação do JDK (ex: `C:\Program Files\Java\jdk-25`).
    - Adicione a entrada `%JAVA_HOME%\bin` à variável de sistema `Path`.
  - **Verifique a instalação** abrindo um novo PowerShell e rodando:
    ```powershell
    java -version
    javac -version
    ```
  </details>

- **Docker Desktop:** Recomendado para subir um banco de dados MongoDB facilmente.

### 2. Configuração do Banco de Dados

A aplicação precisa se conectar a um banco de dados MongoDB. A configuração é feita através de duas variáveis de ambiente:

- `MONGO_URI`: A string de conexão do MongoDB.
- `MONGO_DATABASE`: O nome do banco de dados que será utilizado.

Você tem três opções para configurar seu banco:

---

#### Opção 1: Usar Docker (Recomendado)

Esta é a forma mais simples e rápida de ter um banco de dados rodando, sem precisar instalar o MongoDB na sua máquina.

1.  **Inicie o container do MongoDB:**
    Na raiz do projeto, execute o comando:
    ```bash
    docker-compose up -d
    ```
    Isso irá iniciar um container Docker com o MongoDB pronto para uso na porta `27017`.

2.  **Configure as variáveis de ambiente:**
    Em seu terminal, exporte as seguintes variáveis:
    ```bash
    export MONGO_URI="mongodb://localhost:27017"
    export MONGO_DATABASE="polaris_local_db"
    ```

---

#### Opção 2: Usar uma Instância Local do MongoDB

Se você já possui o MongoDB instalado na sua máquina e rodando na porta padrão (`27017`).

1.  **Configure as variáveis de ambiente:**
    ```bash
    export MONGO_URI="mongodb://localhost:27017"
    export MONGO_DATABASE="polaris_local_db"
    ```

---

#### Opção 3: Usar o Ambiente de Desenvolvimento do Atlas

Temos um ambiente de desenvolvimento compartilhado no MongoDB Atlas (nuvem).

1.  **Solicite o acesso:**
    Entre em contato com um dos mantenedores do projeto para obter a `MONGO_URI` e o `MONGO_DATABASE` do ambiente de desenvolvimento.

2.  **Configure as variáveis de ambiente:**
    Após receber as credenciais, configure-as no seu terminal.
    ```bash
    export MONGO_URI="mongodb+srv://<user>:<password>@cluster-dev..."
    export MONGO_DATABASE="polaris_dev"
    ```

### 3. Rodar a Aplicação

Com o banco de dados configurado (e o Docker rodando, se for sua escolha), inicie a aplicação:

- **Linux/macOS:**
  ```bash
  ./gradlew bootRun
  ```
- **Windows (PowerShell):**
  ```powershell
  .\gradlew.bat bootRun
  ```

A API estará disponível em `http://localhost:8080`, e o Swagger UI em `http://localhost:8080/swagger-ui.html`.

### 4. Comandos Úteis

- **Rodar os testes (Requer Docker):**
  ```bash
  ./gradlew test
  ```

- **Rodar teste sem Docker:**
  ```bash
  ./gradlew test -PexcludeDocker
  ```

- **Gerar o JAR da aplicação:**
  ```bash
  ./gradlew bootJar
  ```
