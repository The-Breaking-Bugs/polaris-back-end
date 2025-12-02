# 📚 Feature: Tasks

**Data de início da feature:** 2025-12-01  
**Data da última atualização da feature:** 2025-12-02   
**Autores:** Maria Eduarda Cordeiro, Ian Barbosa, Danilo Martins de Oliveira, Carlos Eduardo Araujo  
**Status:** ✅ Concluido

## 🧐 Overview:

A **Task** (referida como "Tarefa", "Prova" ou "Trabalho") é a unidade executável do sistema.

O objetivo desta feature é transformar a ansiedade acadêmica de "o que precisa ser entregue?" em clareza imediata. Ela centraliza todos os entregáveis (provas, trabalhos, leituras) e os organiza automaticamente por **urgência** (Data de Entrega). Ao associar uma tarefa a um **Module**, o aluno contextualiza a atividade, garantindo que nenhum prazo seja perdido.

## 🗺️ Index

1. [User Story](#1-user-story)
2. [Functional Requirements](#2-functional-requirements)
3. [Business Rules](#3-business-rules)
4. [API Endpoints](#4-api-endpoints)
5. [TDD & Testing Strategy](#5-tdd--testing-strategy)

## 👨‍🎓 1. User Story

> "Como um **estudante**, eu quero adicionar minhas **Tasks** (provas, trabalhos e atividades), associando-as a um **Module** e definindo uma data de entrega, para que eu saiba exatamente o que precisa ser feito e quando."

## ✅ 2. Functional Requirements

O sistema deve permitir o gerenciamento das `tasks` focando na distinção entre itens pendentes e concluídos:

- <a id="fr-001"></a>**[FR-001] Create Task:** O sistema deve permitir criar uma nova `Task` contendo Título, Descrição (opcional), Data de Entrega (`dueDate`) e a referência ao Módulo associado.

- <a id="fr-002"></a>**[FR-002] List Pending Tasks:** O sistema deve listar todas as tarefas com status **"Pendente"**.

- <a id="fr-003"></a>**[FR-003] List History (Completed):** O sistema deve listar separadamente as tarefas com status **"Concluída"**.

- <a id="fr-004"></a>**[FR-004] Update Task:** O sistema deve permitir editar os dados da tarefa (título, descrição, data).

- <a id="fr-005"></a>**[FR-005] Toggle Status:** O sistema deve permitir marcar uma tarefa como "Concluída" ou revertê-la para "Pendente".

- <a id="fr-006"></a>**[FR-006] Delete Task:** O sistema deve permitir a exclusão permanente de uma tarefa.

- ⁠<a id="fr-007"></a>**[FR-007] Get Task Details:** O sistema deve permitir visualizar os detalhes completos de uma ⁠ Task ⁠ específica pelo seu ID.

## 📜 3. Business Rules

Regras para garantir a integridade dos prazos e a relação com as matérias.

- <a id="br-001"></a>**[BR-001]  Ownership (Propriedade):** Assim como em módulos, toda `Task` deve pertencer a um `ownerId` (via Header `X-User-Id`). O usuário só pode manipular suas próprias tarefas.

- <a id="br-002"></a>**[BR-002] Module Association (Associação de Módulo):** Toda tarefa deve, obrigatoriamente, estar vinculada a um `Module` existente pertencente ao mesmo usuário.

- <a id="br-003"></a>**[BR-003] Active Module Constraint (Restrição de Módulo Ativo):** Não é permitido criar novas tarefas para um `Module` que esteja **Arquivado** (ver **[[Module BR-003](modules.md#br-003)]**). Se o módulo estiver arquivado, a criação deve ser bloqueada.

- <a id="br-004"></a>**[BR-004] Future Date Validation (Validade Temporal):** A `dueDate` (Data de Entrega) deve ser igual ou posterior à data atual no momento da criação. O sistema não deve aceitar tarefas retroativas.

- <a id="br-005"></a>**[BR-005] Urgency Sorting (Ordenação por Urgência):**
    1. A lista de **Pendentes [[FR-002]](#fr-002)** deve ser ordenada de forma **Ascendente** pela Data de Entrega (datas mais próximas/urgentes primeiro).

    2. A lista de **Histórico [[FR-003]](#fr-003)** deve ser ordenada de forma **Descendente** pela Data de Entrega (conclusões mais recentes primeiro).

- <a id="br-006"></a>**[BR-006] Default Status (Status Padrão):** Toda tarefa nasce com o status `PENDING` por padrão.

## 🔌 4. API Endpoints

**Base URL:** `/tasks`

| Método | Endpoint | Descrição | Status Sucesso | Erros Comuns |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/` | Cria uma nova tarefa. | `201 Created` | `400 Bad Request` (data passada), `404 Not Found` (módulo não existe/arquivado) |
| **GET** | `/` | Lista tarefas **Pendentes** (Padrão) ordenadas por urgência. Aceita query param `?status=COMPLETED` para histórico. | `200 OK` | `401 Unauthorized` |
| **GET** | `/{id}` |Detalhes de uma tarefa. | `200 OK` | `404 Not Found`, `403 Forbidden` |
| **PUT** | `/{id}` | Atualiza dados da tarefa. | `200 OK` | `400 Bad Request` |
| **PATCH** | `/{id}/status` | Alterna entre PENDING/COMPLETED. | `200 OK` | `404 Not Found` |
| **DELETE**| `/{id}` | Exclusão permanente. | `204 No Content` | `404 Not Found`|

## 🧪 5. TDD & Testing Strategy

A estratégia de testes foca na validação temporal e na integridade relacional com os Módulos.

### Como Rodar os Testes

```bash
./gradlew test --tests "com.thebreakingbugs.polaris_back_end.tasks.*"
```
### Cenários Críticos de Teste

#### Service Layer (Regras de Negócio):
`src/test/.../tasks/service/TaskServiceTest.java`

- **Date Validation:** Tentar criar tarefa com data de ontem deve lançar `IllegalArgumentException` **([[BR-004]](#br-004))**.

- **Module Integrity:** Tentar criar tarefa para um `moduleId` inexistente ou que pertence a outro usuário deve falhar com `SecurityException` ou `NoSuchElementException` **([[BR-002]](#br-002))**.

- **Archived Check:** Tentar criar tarefa para um módulo Arquivado deve lançar `IllegalArgumentException` **([[BR-003]](#br-003))**.

- **Sorting Logic:** Validar se o método `listPending` retorna a lista ordenada corretamente (data menor no índice 0).

#### Controller Layer (API Contract):
`ssrc/test/.../tasks/controller/TaskControllerTest.java`

- Validar payloads de Request Body (campos obrigatórios: `title`, `dueDate`, `moduleId`).

- Garantir que o `status` (PENDING/COMPLETED) pode ser filtrado via query param.

- Validar respostas 200/201 e tratamento de exceções (400/404).
