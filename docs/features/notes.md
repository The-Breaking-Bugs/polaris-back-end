# 📚 Feature: Notes

**Data de início da feature:** 2025-12-02  
**Data da última atualização da feature:** 2025-12-02  
**Autores:** Gabriel Paes  
**Status:** 🚧 Em Desenvolvimento

## 🧐 Overview:

A **Note** (referida como "Nota", "Resumo" ou "Anotação") é onde o conhecimento é registrado.

Esta feature transforma o aprendizado disperso em um repositório centralizado de conhecimento. O objetivo é fornecer ao estudante um local robusto para criar resumos estruturados, capturar insights de aulas e organizar o material de estudo, mantendo tudo estritamente vinculado ao contexto das **Matérias (Modules)**.

## 🗺️ Index

1. [User Story](#1-user-story)
2. [Functional Requirements](#2-functional-requirements)
3. [Business Rules](#3-business-rules)
4. [API Endpoints](#4-api-endpoints)
5. [TDD & Testing Strategy](#5-tdd--testing-strategy)

## 👨‍🎓 1. User Story

> "Como um **estudante**, eu quero criar notas e resumos sobre uma aula específica (ex: 'Resumo Aula 5 - Qualidade Total') e vinculá-las à matéria, para que eu possa revisar o conteúdo depois."

## ✅ 2. Functional Requirements

O sistema deve gerenciar a criação, visualização e busca de conteúdo textual:

  - \<a id="fr-001"\>\</a\>**[FR-001] Create Note:** O usuário deve poder criar uma `Note` contendo Título e Conteúdo, obrigatoriamente vinculada a um Módulo.

  - \<a id="fr-002"\>\</a\>**[FR-002] List Notes:** O usuário deve poder listar suas notas. A listagem deve permitir filtragem por `moduleId` (ver notas de uma matéria específica) e ordenação por data de criação.

  - \<a id="fr-003"\>\</a\>**[FR-003] Search Notes (Full-Text):** O usuário deve poder pesquisar por termos específicos contidos tanto no **Título** quanto no **Conteúdo** das notas.

  - \<a id="fr-004"\>\</a\>**[FR-004] Get Note Details:** O sistema deve permitir visualizar o conteúdo completo de uma nota.

  - \<a id="fr-005"\>\</a\>**[FR-005] Update Note:** O usuário deve poder editar o título e o conteúdo de uma nota existente.

  - \<a id="fr-006"\>\</a\>**[FR-006] Delete Note:** O usuário deve poder excluir uma nota permanentemente.

## 📜 3. Business Rules

Regras focadas na organização do conteúdo e capacidade de busca.

  - \<a id="br-001"\>\</a\>**[BR-001] Module Association (Vínculo Obrigatório):** Toda Nota deve ser vinculada a um `Module` existente do usuário. Notas "órfãs" (sem matéria) não são permitidas, pois quebram a organização por contexto.

  - \<a id="br-002"\>\</a\>**[BR-002] Content Limits (Limites de Conteúdo):**

    1.  O **Título** deve ser limitado a 100 caracteres.
    2.  O **Conteúdo** deve suportar grandes volumes de texto (armazenamento tipo TEXT/CLOB ou documento BSON flexível), permitindo formatação básica (Markdown ou HTML sanitizado).

  - \<a id="br-003"\>\</a\>**[BR-003] Duplicate Warning (Duplicidade Permissiva):** Diferente de módulos, o sistema **NÃO** deve impedir a criação de duas notas com o mesmo título na mesma matéria (ex: duas notas chamadas "Revisão").

    > *Nota de UX:* Embora o Backend permita, o Frontend deve alertar o usuário sobre a possível duplicidade.

  - \<a id="br-004"\>\</a\>**[BR-004] Search Indexing (Indexação):** Todo o conteúdo de texto (Título e Corpo) deve ser indexado imediatamente após a criação ou edição para garantir que a busca **[[FR-003]](#fr-003)** retorne resultados atualizados.

  - \<a id="br-005"\>\</a\>**[BR-005] Ownership:** Assim como nos demais recursos, o usuário só pode criar, listar ou buscar notas vinculadas ao seu próprio `ownerId`.

## 🔌 4. API Endpoints

**Base URL Contextual:** Os endpoints das notas são acessados aninhados a um `Module` específico.

| Método | Endpoint | Descrição | Status Sucesso | Erros Comuns |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/modules/{moduleId}/notes` | Cria uma nova nota para o módulo especificado. | `201 Created` | `400 Bad Request` (título longo) `404 Not Found` (Module) |
| **GET** | `/modules/{moduleId}/notes` | Lista todas as notas do módulo especificado. | `200 OK` | `401 Unauthorized` `404 Not Found` (Module) |
| **GET** | `/modules/{moduleId}/notes/search` | Pesquisa textual nas notas do módulo especificado. Ex: `?q=teorema`. | `200 OK` | `400 Bad Request` (query vazia) |
| **GET** | `/modules/{moduleId}/notes/{noteId}` | Retorna o conteúdo completo de uma nota específica de um módulo. | `200 OK` | `404 Not Found` (Module/Note) |
| **PUT** | `/modules/{moduleId}/notes/{noteId}` | Atualiza o título e/ou conteúdo de uma nota específica de um módulo. | `200 OK` | `400 Bad Request` `404 Not Found` (Module/Note) |
| **DELETE**| `/modules/{moduleId}/notes/{noteId}` | Exclui permanentemente uma nota específica de um módulo. | `204 No Content` | `404 Not Found` (Module/Note) |

> **Nota Técnica:** O endpoint de busca (`/search`) deve utilizar mecanismos otimizados (como Text Index do MongoDB) para varrer título e conteúdo.

## 🧪 5. TDD & Testing Strategy

A estratégia de testes deve focar na persistência de grandes textos e na eficácia da busca.

### Como Rodar os Testes

```bash
./gradlew test --tests "com.thebreakingbugs.polaris_back_end.notes.*"
```

### Cenários Críticos de Teste

#### Service Layer (Regras de Negócio):

`src/test/.../notes/service/NoteServiceTest.java`

  - **Character Limit:** Tentar salvar nota com título \> 100 chars deve lançar exceção **([[BR-002]](#br-002))**.
  - **Module Link:** Tentar salvar nota sem `moduleId` deve falhar **([[BR-001]](#br-001))**.
  - **Large Content:** Testar persistência de um texto grande (ex: 5.000 caracteres) para garantir que o banco não trunca os dados.

#### Search Logic (Integration):

`src/test/.../notes/service/NoteSearchTest.java`

  - **Keyword Matching:**
    1.  Criar nota A com conteúdo "O Teorema de Pitágoras é fundamental".
    2.  Criar nota B com conteúdo "Bhaskara resolve equações".
    3.  Buscar por "Pitágoras".
    4.  **Assert:** A lista deve conter apenas Nota A.
  - **Empty Results:** Buscar por uma palavra inexistente deve retornar lista vazia (não null).
  - **Module Filter:** Criar notas no Módulo X e Y. Filtrar por Módulo X e garantir que notas de Y não retornam.
