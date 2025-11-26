# 📚 Feature: Modules

**Data de início da feature:** 2025-11-25  
**Data da última atualização da feature:** 2025-11-25  
**Autor:** Gabriel Paes  
**Status:** 🚧 Em Desenvolvimento

## 🧐 Overview:

O **Module** (referido coloquialmente como "Matéria" ou "Disciplina") é a entidade central para a organização da vida acadêmica do usuário.

O objetivo desta feature é permitir que o usuário cadastre as matérias que está cursando (ex: 'Gestão e Qualidade', 'Cálculo II'). Isso servirá de base para agrupar futuras tarefas, notas e prazos. Sem módulos, o usuário não consegue organizar o restante das informações.

## 🗺️ Index

1. [User Story](#1-user-story)
2. [Functional Requirements](#2-functional-requirements)
3. [Business Rules](#3-business-rules)
4. [API Endpoints](#4-api-endpoints)
5. [TDD & Testing Strategy](#5-tdd--testing-strategy)

## 👨‍🎓 1. User Story

> "Como um **estudante**, eu quero cadastrar meus **Modules**, para que eu possa agrupar minhas tarefas e notas por contexto."

## ✅ 2. Functional Requirements

O sistema deve permitir um CRUD básico para a entidade `Module`:

- <a id="fr-001"></a>**[FR-001] Create Module:** O sistema deve permitir que um usuário crie um novo `Module`.

- <a id="fr-002"></a>**[FR-002] List Active Modules:** O sistema deve listar todos os `Modules` ativos pertencentes ao usuário (pelo `ownerId`).

- <a id="fr-003"></a>**[FR-003] Get Module Details:** O sistema deve permitir visualizar detalhes de um `Module` específico.

- <a id="fr-004"></a>**[FR-004] Update Module:** O sistema deve permitir alterar nome, descrição e cor de um `Module`.

- <a id="fr-005"></a>**[FR-005] Archive Module:** O sistema deve permitir arquivar um `Module` ao invés de excluí-lo permanentemente, preservando o histórico de notas e tarefas vinculadas.

- <a id="fr-006"></a>**[FR-006] Delete Module:** O sistema deve permitir a exclusão permanente apenas se o usuário confirmar explicitamente (utilizado para erros de cadastro).

## 📜 3. Business Rules

As regras abaixo definem as restrições e comportamentos esperados para garantir a consistência dos dados.

- <a id="br-001"></a>**[BR-001]  Ownership (Propriedade do Dado):** Todo `Module` deve obrigatoriamente ter um `ownerId` vinculado no momento da criação. Nenhuma operação de edição ou exclusão pode ser realizada se o `ownerId` da requisição não corresponder ao dono do recurso.

- <a id="br-002"></a>**[BR-002] Module Uniqueness (Unicidade):** Não é permitido cadastrar dois módulos com o mesmo `name` para o mesmo `ownerId`.

    > Ex: O usuário A pode ter "Cálculo I" e o usuário B também, mas o usuário A não pode ter dois "Cálculo I".

- <a id="br-003"></a>**[BR-003] Archiving Strategy (Estratégia de Arquivamento):** Ao arquivar um módulo:

    1. Ele deve deixar de aparecer na listagem principal **[[FR-002]](#fr-002)**.

    2. Ele não deve ser excluído do banco de dados.

    3. Novas tarefas não podem ser criadas para módulos arquivados.

- <a id="br-004"></a>**[BR-004] Visual Identification (Identificação Visual):** O sistema deve aceitar um código de cor (Hexadecimal) para o módulo. Caso o usuário não envie uma cor na criação, o sistema deve atribuir uma cor padrão ou aleatória para diferenciar visualmente na interface.

- <a id="br-005"></a>**[BR-005] Mandatory Fields (Campos Obrigatórios):** Apenas o `name` e o `ownerId` são estritamente obrigatórios para a criação **([[FR-001]](#fr-001))**. Descrição e cor são opcionais.

## 🔌 4. API Endpoints

A comunicação seguirá o padrão RESTful.
**Base URL:** `/modules`

| Método | Endpoint | Descrição | Status Sucesso | Erros Comuns |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/` | Cria um novo módulo. | `201 Created` | `400 Bad Request` (dado inválido), `409 Conflict` (nome já existe) |
| **GET** | `/` | Lista módulos do usuário. | `200 OK` | `401 Unauthorized` (header `X-User-Id` ausente) |
| **GET** | `/{id}` | Detalhes de um módulo. | `200 OK` | `404 Not Found`, `403 Forbidden` (não é o dono) |
| **PUT** | `/{id}` | Atualiza dados do módulo. | `200 OK` | `400 Bad Request`, `404 Not Found`, `403 Forbidden` |
| **PATCH** | `/{id}/archive` | Arquiva/Desarquiva o módulo. | `200 OK` | `404 Not Found`, `403 Forbidden` |
| **DELETE**| `/{id}` | Exclusão permanente. | `204 No Content` | `404 Not Found`, `403 Forbidden` |

> **Nota Técnica sobre Autenticação:** Atualmente, a identificação do usuário (`ownerId`) é realizada através do Header HTTP `X-User-Id`. Esta é uma **medida provisória** para facilitar o desenvolvimento inicial. Em uma futura implementação, este mecanismo será substituído por um sistema de autenticação robusto (como JWT), onde o `ownerId` será extraído diretamente do token do usuário autenticado, garantindo mais segurança e tratando erros como `401 Unauthorized` / `403 Forbidden` de forma mais segura.

## 🧪 5. TDD & Testing Strategy

Esta feature foi desenvolvida seguindo estritamente a metodologia **TDD (Test Driven Development)**.
Para validar a implementação e as Regras de Negócio, execute os testes específicos deste domínio.

### Como Rodar os Testes (How to Run)
Para executar a bateria de testes apenas do domínio de módulos:

```bash
./gradlew test --tests "com.thebreakingbugs.polaris_back_end.modules.*"
```
### Arquivos de Teste Relevantes (Key Test Files)
A cobertura de testes está dividida em camadas para isolar responsabilidades:

#### Service Layer (Regras de Negócio):
`src/test/.../modules/service/ModuleServiceTest.java`

- Valida a criação de módulos únicos **([[BR-002]](#br-002))**.

- Valida a obrigatoriedade do `ownerId` **([[BR-001]](#br-001))**.

- Garante que módulos arquivados não sejam retornados na listagem padrão.

#### Controller Layer (API Contract):
`src/test/.../modules/controller/ModuleControllerTest.java`

- Valida os Status Codes HTTP (201 Created, 400 Bad Request).

- Garante que a serialização do JSON de entrada e saída está correta.
