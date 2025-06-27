CREATE TABLE "Conta" (
  "id" uuid PRIMARY KEY,
  "username" varchar(25) UNIQUE NOT NULL,
  "email" varchar(50) UNIQUE NOT NULL,
  "senha" varchar(20) NOT NULL,
  "nome" varchar(150) NOT NULL,
  "endereco_id" uuid NOT NULL
);

CREATE TABLE "Candidato" (
  "conta_id" uuid PRIMARY KEY,
  "cpf" varchar(11) UNIQUE NOT NULL,
  "bio" varchar(250),
  "tipo_deficiencia" enum
);

CREATE TABLE "Empresa" (
  "conta_id" uuid PRIMARY KEY,
  "cnpj" varchar(30) UNIQUE NOT NULL,
  "descricao" varchar(250),
  "foto_perfil" text
);

CREATE TABLE "Contato" (
  "id" uuid PRIMARY KEY,
  "telefone1" varchar(15) NOT NULL,
  "telefone2" varchar(15),
  "candidato_id" uuid,
  "empresa_id" uuid
);

CREATE TABLE "Endereco" (
  "id" uuid PRIMARY KEY,
  "rua" varchar(250) NOT NULL,
  "bairro" varchar(250) NOT NULL,
  "cidade" varchar(250) NOT NULL,
  "estado" varchar(2) NOT NULL,
  "cep" varchar(8) NOT NULL,
  "numero" varchar(10) NOT NULL,
  "complemento" varchar(250),
  "ponto_referencia" varchar(250),
  "pais" varchar(250) NOT NULL
);

CREATE TABLE "Vaga" (
  "id" uuid PRIMARY KEY,
  "empresa_id" uuid NOT NULL,
  "nome_cargo" varchar(250) NOT NULL,
  "descricao" varchar(250),
  "logo_empresa" text,
  "status_vaga" boolean NOT NULL DEFAULT true,
  "data_fim_candidatura" date NOT NULL,
  "data_fim_ultima_etapa" date NOT NULL
);

CREATE TABLE "vaga_tags" (
  "vaga_id" uuid,
  "tag" varchar
);

CREATE TABLE "Candidatura" (
  "id" uuid PRIMARY KEY,
  "candidato_id" uuid NOT NULL,
  "vaga_id" uuid NOT NULL,
  "status_candidatura" boolean NOT NULL DEFAULT true,
  "data_criacao" timestamp NOT NULL
);

CREATE TABLE "Habilidade" (
  "id" uuid PRIMARY KEY,
  "nome" varchar(50) NOT NULL,
  "anosExperiencia" integer NOT NULL,
  "candidato_id" uuid NOT NULL
);

ALTER TABLE "Conta" ADD FOREIGN KEY ("endereco_id") REFERENCES "Endereco" ("id");

ALTER TABLE "Candidato" ADD FOREIGN KEY ("conta_id") REFERENCES "Conta" ("id");

ALTER TABLE "Empresa" ADD FOREIGN KEY ("conta_id") REFERENCES "Conta" ("id");

ALTER TABLE "Contato" ADD FOREIGN KEY ("candidato_id") REFERENCES "Candidato" ("conta_id");

ALTER TABLE "Contato" ADD FOREIGN KEY ("empresa_id") REFERENCES "Empresa" ("conta_id");

ALTER TABLE "Vaga" ADD FOREIGN KEY ("empresa_id") REFERENCES "Empresa" ("conta_id");

ALTER TABLE "vaga_tags" ADD FOREIGN KEY ("vaga_id") REFERENCES "Vaga" ("id");

ALTER TABLE "Candidatura" ADD FOREIGN KEY ("candidato_id") REFERENCES "Candidato" ("conta_id");

ALTER TABLE "Candidatura" ADD FOREIGN KEY ("vaga_id") REFERENCES "Vaga" ("id");

ALTER TABLE "Habilidade" ADD FOREIGN KEY ("candidato_id") REFERENCES "Candidato" ("conta_id");
