# PcDev – Transformando Potenciais em Oportunidades


## 🚀 Sobre o Projeto

**PcDev** é uma plataforma web desenvolvida como projeto final para o curso Recode Pro, com o objetivo de gerar impacto social real através da tecnologia. A solução visa conectar talentos diversos, com foco em **Pessoas com Deficiência (PCD)**, às melhores oportunidades de vagas de emprego e cursos de capacitação no mercado de trabalho.

Acreditamos que a inclusão não é apenas um direito, mas a força motriz para uma sociedade mais inovadora, justa e produtiva. No Brasil, milhões de pessoas com deficiência possuem um potencial extraordinário, mas muitas vezes enfrentam barreiras na busca por qualificação e emprego. O PcDev nasceu para romper essas barreiras, oferecendo um espaço dedicado onde a capacitação encontra a oportunidade, e a diversidade é valorizada.

Este projeto foi desenvolvido seguindo os requisitos da **Versão Completa (Desafio Avançado)**.

---

## 🎯 Público-Alvo e Objetivos de Desenvolvimento Sustentável (ODS)

-   **Público-Alvo:** Pessoas com Deficiência (PCD).
-   **ODS da ONU alinhadas ao projeto:**
    -   **ODS 4 – Educação de Qualidade:** Assegurar uma educação inclusiva, equitativa e de qualidade.
    -   **ODS 8 – Trabalho Decente e Crescimento Econômico:** Promover o crescimento econômico inclusivo e sustentável.
    -   **ODS 10 – Redução das Desigualdades:** Reduzir as desigualdades dentro e entre os países.

---

## ✨ Funcionalidades Principais

-   **Autenticação e Autorização:** Sistema de login seguro com Spring Security para proteger rotas e dados.
-   **Perfis de Usuário:** Distinção entre usuários comuns e administradores, com permissões específicas.
-   **Gerenciamento para Administradores:** Um dashboard completo onde administradores podem realizar operações de CRUD (Criar, Ler, Atualizar, Deletar) para:
    -   Vagas de Emprego
    -   Cursos de Capacitação
    -   Usuários da plataforma
-   **Acessibilidade:** Interface com opção de modo de alto contraste para melhorar a experiência de navegação.
-   **Busca de Oportunidades:** Usuários podem visualizar e buscar vagas e cursos disponíveis.
-   **Configurações de Conta:** Usuários logados podem gerenciar suas informações de perfil.

---

## 🛠️ Tecnologias Utilizadas

O projeto foi construído com uma arquitetura de microsserviços, separando o front-end do back-end para maior escalabilidade e manutenção.

### **Front-end (pcdevapp)**

-   **[React.js](https://reactjs.org/)**: Biblioteca JavaScript para construção de interfaces de usuário.
-   **[React Router](https://reactrouter.com/)**: Para gerenciamento de rotas na aplicação.
-   **[Bootstrap](https://getbootstrap.com/)**: Framework CSS para design responsivo e componentes estilizados.
-   **[CSS3](https://developer.mozilla.org/pt-BR/docs/Web/CSS)**: Para estilizações personalizadas e acessibilidade.

### **Back-end (pcdevapi)**

-   **[Java 24](https://www.oracle.com/java/)**: Linguagem de programação principal.
-   **[Spring Boot](https://spring.io/projects/spring-boot)**: Framework para criação de aplicações Java robustas e independentes.
-   **[Spring MVC](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)**: Para construir a API RESTful seguindo o padrão MVC.
-   **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**: Para persistência de dados e comunicação com o banco de dados.
-   **[Spring Security](https://spring.io/projects/spring-security)**: Para implementar autenticação e autorização.
-   **[PostgreSQL](https://www.mysql.com/)**: Sistema de gerenciamento de banco de dados relacional.
-   **[Maven](https://maven.apache.org/)**: Ferramenta de gerenciamento de dependências e build do projeto.

---

## 🔗 Links Úteis

### Repositórios do Projeto

-   **Backend (Esta API):** [https://github.com/Squad-17-Recode/pcdevapi](https://github.com/Squad-17-Recode/pcdevapi)
-   **Frontend:** [https://github.com/Squad-17-Recode/pcdevapp](https://github.com/Squad-17-Recode/pcdevapp)
-   **Organização do Squad no GitHub:** [https://github.com/orgs/Squad-17-Recode/repositories](https://github.com/orgs/Squad-17-Recode/repositories)

---

## ⚙️ Como Executar o Projeto Localmente

### **Pré-requisitos**

-   Node.js e npm/yarn
-   Java JDK 24 ou superior
-   Maven
-   Git

### **1. Back-end (API)**

#### **Passo 1: Clone o repositório**

```bash
# Clone o repositório do back-end
git clone https://github.com/Squad-17-Recode/pcdevapi.git

# Navegue até o diretório do projeto
cd pcdevapi
```

#### **Passo 2: Configure as variáveis de ambiente**

Crie um arquivo `.env` na raiz do projeto (mesmo diretório onde está o arquivo `pom.xml`) com o seguinte conteúdo:

```env
# Database Configuration
DATABASE_URL=jdbc:postgresql://pcdevapi-test-pcdevapi.f.aivencloud.com:17502/defaultdb?ssl=require&user=avnadmin&password=AVNS_2Pg24xgocY0c9XIe89U
DATABASE_USERNAME=avnadmin
DATABASE_PASSWORD=AVNS_2Pg24xgocY0c9XIe89U

# JWT Configuration
JWT_SECRET=aHRZAcjtlf2fLfnqwrC6O/GeH5MdjH8ruBnQKhMZpBU=
JWT_EXPIRATION=43200000

# Server Configuration
SERVER_PORT=8080
```

#### **Passo 3: Instale as dependências e execute**

```bash
# Instale as dependências com o Maven
mvn clean install

# Execute a aplicação
mvn spring-boot:run
```

> ✅ A API estará rodando em `http://localhost:8080`

### **2. Front-end (Aplicação React)**

#### **Passo 1: Clone o repositório do front-end**

```bash
# Em um novo terminal, clone o repositório do front-end
git clone https://github.com/Squad-17-Recode/pcdevapp.git

# Navegue até o diretório do projeto
cd pcdevapp
```

#### **Passo 2: Instale as dependências e execute**

```bash
# Instale as dependências
npm install

# Inicie o servidor de desenvolvimento
npm run dev
```

> ✅ A aplicação estará acessível em `http://localhost:5173` (ou outra porta se esta estiver ocupada)

### **3. Testando a aplicação**

1. **Acesse o frontend** em `http://localhost:5173`
2. **Crie uma conta** usando o formulário de cadastro
3. **Faça login** com as credenciais criadas
4. **Explore as funcionalidades** da plataforma

---

## 👨‍💻 Squad 17

-   Igor Yonezawa
-   Larissa de Araújo Dias da Silva
-   Lauren Freire Monteles
-   Lucas Pires Esteves Costa
-   Matheus Moreira Lima
-   Valquiria Coelho Lima Galeno
