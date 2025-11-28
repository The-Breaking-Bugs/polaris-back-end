# Polaris Back-end

Um app de organização acadêmica que centraliza tarefas, lembretes e notas, com a visão futura de ser um assistente proativo de IA.

## Documentações

**[Feature Modules](docs/features/modules.md)**

## Guia do ambiente de desenvolvimento

### Pré-requisitos

- Java JDK 25 instalado (JDK 25 recomendado).
- Docker Desktop (se você for rodar testes que usam containers).

### Passos (Windows)


1. Instale o JDK 25

   - Baixe e instale o JDK 25: https://www.oracle.com/java/technologies/downloads/#java25

   - Após a instalação, configure as variáveis de ambiente do Windows:

     - `JAVA_HOME` apontando para o diretório do JDK (ex.: `C:\Program Files\Java\jdk-25`).
     - Adicione `%JAVA_HOME%\bin` ao `Path`.

   - Verifique a instalação no PowerShell:

     ```powershell
     java -version
     javac -version
     ```

2. Inicie o Docker Desktop (opcional)

   - Abra o Docker Desktop e confirme que está rodando (ícone na barra de tarefas). Se o projeto usar Testcontainers, o Docker precisa estar ativo.

3. Rodar a aplicação (modo desenvolvimento)

   - No PowerShell (na raiz do projeto):

     ```powershell
     .\gradlew.bat bootRun
     ```

4. Gerar JAR e executar (opcional)

   - Gerar o JAR:

     ```powershell
     .\gradlew.bat bootJar
     ```

   - Executar o JAR gerado (ex.: `build\libs\polaris-back-end-<versao>.jar`):

     ```powershell
     java -jar build\libs\*-SNAPSHOT.jar
     ```

5. Rodar testes

   - No PowerShell:

     ```powershell
     .\gradlew.bat test
     ```

### Resumo rápido de comandos (Windows)

```powershell
# verificar Java
java -version
javac -version

# iniciar Docker Desktop manualmente (via UI)

# rodar em modo dev
.\gradlew.bat bootRun

# gerar JAR
.\gradlew.bat bootJar

# rodar testes
.\gradlew.bat test
```
