# 🚗 Sistema de Gestão de Vendas de Veículos

> API REST moderna para gerenciamento de vendedores, veículos e vendas, construída com Spring Boot 4.0 e interface web responsiva.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📋 Sumário

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [Executando o Projeto](#-executando-o-projeto)
- [Endpoints da API](#-endpoints-da-api)
- [Interface Web](#-interface-web)
- [Testes](#-testes)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Contribuindo](#-contribuindo)

---

## 🎯 Sobre o Projeto

Sistema completo de gestão de vendas de veículos que permite cadastrar vendedores, gerenciar estoques de veículos e registrar vendas. Inclui validações robustas, tratamento de erros centralizado e uma interface web moderna e responsiva.

### 🌟 Destaques

- ✅ **API REST** com padrão de DTOs
- ✅ **Validação de dados** com Bean Validation
- ✅ **Tratamento global de exceções**
- ✅ **Interface web moderna** com design Ocean Breeze Professional
- ✅ **Testes unitários** com JUnit 5 e Mockito
- ✅ **Relacionamentos JPA** bem estruturados

---

## 🚀 Funcionalidades

### 👥 Gestão de Vendedores
- Cadastrar, listar, atualizar e deletar vendedores
- Validação de email e telefone únicos
- Visualizar veículos por vendedor
- Proteção contra deleção de vendedores com veículos

### 🚘 Gestão de Veículos
- Cadastrar, listar, atualizar e deletar veículos
- Validação de placa (formatos antigo e Mercosul)
- Associação automática a vendedores
- Bloqueio de edição/deleção de veículos vendidos
- Marcação automática de status de venda

### 💰 Gestão de Vendas
- Registrar vendas com validação de disponibilidade
- Histórico completo de vendas
- Marcação automática de veículos como vendidos
- Valor final baseado no preço do veículo
- Data automática de registro

---

## 🛠️ Tecnologias

### Backend
- **Java 17**
- **Spring Boot 4.0.0**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - Spring DevTools
- **MySQL 8.0+**
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

### Frontend
- **HTML5 / CSS3**
- **JavaScript (Vanilla)**
- **Tailwind CSS** - Framework CSS
- **Lucide Icons** - Ícones modernos
- **Google Fonts** (Outfit & Inter)

### Testes
- **JUnit 5**
- **Mockito**
- **Spring Boot Test**

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [MySQL 8.0+](https://dev.mysql.com/downloads/)
- [Maven 3.9+](https://maven.apache.org/download.cgi) (ou use o Maven Wrapper incluído)
- [Git](https://git-scm.com/)

---

## 📥 Instalação

1. **Clone o repositório**

```bash
git clone https://github.com/douglas-reinaldo/APIVendasVeiculosProject.git
cd APIVendasVeiculosProject
```

2. **Configure o banco de dados MySQL**

Crie um banco de dados ou deixe o Hibernate criar automaticamente:

```sql
CREATE DATABASE VendasVeiculoBD;
```

3. **Configure as credenciais do banco**

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/VendasVeiculoBD?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

---

## ⚙️ Configuração

### Configurações do `application.properties`

```properties
# Nome da Aplicação
spring.application.name=minha-api-vendas

# Configurações do MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/VendasVeiculoBD?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configurações do JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Portas Padrão

- **API**: `http://localhost:8080`
- **Interface Web**: `http://localhost:8080/vendedores.html`

---

## ▶️ Executando o Projeto

### Usando Maven Wrapper (Recomendado)

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

### Usando Maven instalado

```bash
mvn spring-boot:run
```

### Usando IDE

Abra o projeto na sua IDE (IntelliJ, Eclipse, VSCode) e execute a classe `MinhaApiVendasApplication.java`.

### Acessando a aplicação

Após iniciar, acesse:
- **API**: http://localhost:8080/api
- **Interface Web**: http://localhost:8080/vendedores.html

---

## 📡 Endpoints da API

### 👥 Vendedores

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/vendedores` | Lista todos os vendedores |
| `GET` | `/api/vendedores/{id}` | Busca vendedor por ID |
| `POST` | `/api/vendedores` | Cadastra novo vendedor |
| `PUT` | `/api/vendedores/{id}` | Atualiza vendedor |
| `DELETE` | `/api/vendedores/{id}` | Remove vendedor |
| `GET` | `/api/vendedores/{id}/veiculos` | Lista veículos do vendedor |

**Exemplo de Request Body (POST/PUT):**
```json
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "telefone": "(11) 98888-7777"
}
```

---

### 🚘 Veículos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/veiculos` | Lista todos os veículos |
| `GET` | `/api/veiculos/{id}` | Busca veículo por ID |
| `POST` | `/api/veiculos` | Cadastra novo veículo |
| `PUT` | `/api/veiculos/{id}` | Atualiza veículo |
| `DELETE` | `/api/veiculos/{id}` | Remove veículo |

**Exemplo de Request Body (POST/PUT):**
```json
{
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2024,
  "preco": 120000.00,
  "placa": "ABC-1234",
  "vendedorId": 1
}
```

---

### 💰 Vendas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/vendas` | Lista todas as vendas |
| `GET` | `/api/vendas/{id}` | Busca venda por ID |
| `POST` | `/api/vendas` | Registra nova venda |

**Exemplo de Request Body (POST):**
```json
{
  "veiculoId": 1,
  "vendedorId": 1
}
```

---

## 🖥️ Interface Web

A aplicação inclui uma interface web moderna e responsiva:

### Páginas disponíveis

- **`vendedores.html`** - Gestão de vendedores
- **`veiculos_vendedor.html`** - Gestão de veículos por vendedor
- **`confirmar_venda.html`** - Confirmação de vendas
- **`vendas.html`** - Histórico de vendas

### Design System

O projeto utiliza um design system personalizado chamado **Ocean Breeze Professional**:

- 🎨 Paleta de cores moderna (azul oceano + verde água)
- ✨ Animações suaves e transições
- 📱 Totalmente responsivo
- ♿ Acessível e intuitivo
- 🎭 Componentes reutilizáveis

---

## 🧪 Testes

O projeto possui cobertura de testes unitários para os serviços principais.

### Executando os testes

```bash
# Todos os testes
./mvnw test

# Testes de um serviço específico
./mvnw test -Dtest=VeiculoServiceTest

# Com relatório de cobertura
./mvnw test jacoco:report
```

### Estrutura de Testes

```
src/test/java/
└── com/example/minha_api_vendas/
    └── service/
        ├── VeiculoServiceTest.java
        ├── VendedorServiceTest.java
        └── VendaServiceTest.java
```

---

## 📁 Estrutura do Projeto

```
minha-api-vendas/
├── src/
│   ├── main/
│   │   ├── java/com/example/minha_api_vendas/
│   │   │   ├── controller/          # Controllers REST
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Tratamento de exceções
│   │   │   ├── model/               # Entidades JPA
│   │   │   ├── repository/          # Repositórios JPA
│   │   │   ├── service/             # Lógica de negócio
│   │   │   └── MinhaApiVendasApplication.java
│   │   └── resources/
│   │       ├── static/              # Interface Web
│   │       │   ├── scripts/         # JavaScript
│   │       │   ├── styles/          # CSS
│   │       │   └── *.html           # Páginas HTML
│   │       └── application.properties
│   └── test/                        # Testes unitários
├── pom.xml
└── README.md
```

---

## 🎨 Funcionalidades da Interface

### ✅ Cards Interativos
- Hover effects elegantes
- Animações de entrada
- Estados visuais claros

### ✅ Modais Modernos
- Abertura/fechamento suaves
- Formulários validados
- Feedback em tempo real

### ✅ Estados de Loading
- Spinners animados
- Mensagens contextuais
- Empty states informativos

### ✅ Alerts e Notificações
- Sucesso, erro, warning
- Ícones intuitivos
- Auto-dismiss opcional

---

## 🔐 Validações

### Vendedor
- ✅ Nome: 3-50 caracteres
- ✅ Email: formato válido e único
- ✅ Telefone: formato `(XX) XXXXX-XXXX` e único

### Veículo
- ✅ Marca: 2-50 caracteres
- ✅ Modelo: 2-50 caracteres
- ✅ Ano: entre 1900 e 2030
- ✅ Preço: maior que zero
- ✅ Placa: formatos ABC-1234 ou ABC1D23 (Mercosul)

### Venda
- ✅ Veículo deve existir e não estar vendido
- ✅ Vendedor deve existir
- ✅ Data automática (LocalDate.now())
- ✅ Valor baseado no preço do veículo

---

## 🐛 Tratamento de Erros

A API retorna erros estruturados:

```json
{
  "timestamp": "2024-11-30T10:30:00",
  "status": 400,
  "mensagem": "Erro de validação",
  "erros": {
    "email": "Email inválido",
    "telefone": "Telefone deve estar no formato (XX) XXXXX-XXXX"
  }
}
```

### Códigos HTTP

- `200` - Sucesso
- `201` - Criado
- `204` - Sem conteúdo (deleção bem-sucedida)
- `400` - Erro de validação
- `404` - Recurso não encontrado
- `409` - Conflito (email/placa duplicados)
- `500` - Erro interno do servidor

---

## 🤝 Contribuindo

Contribuições são muito bem-vindas! Siga os passos:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

**⭐ Se este projeto foi útil, considere dar uma estrela no GitHub!**
