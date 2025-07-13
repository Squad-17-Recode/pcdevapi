-- Create Endereco table
CREATE TABLE endereco (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    rua VARCHAR(250) NOT NULL,
    bairro VARCHAR(250) NOT NULL,
    cidade VARCHAR(250) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(8) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(250),
    ponto_referencia VARCHAR(250),
    pais VARCHAR(250) NOT NULL
);

-- Create Role enum type
CREATE TYPE role_enum AS ENUM ('CANDIDATO', 'EMPRESA');

-- Create TipoDeficiencia enum type
CREATE TYPE tipodeficiencia_enum AS ENUM ('AUDITIVA', 'VISUAL', 'FISICA', 'INTELECTUAL', 'MULTIPLA', 'OUTRA');

-- Create StatusCandidatura enum type
CREATE TYPE statuscandidatura_enum AS ENUM ('PENDENTE', 'ACEITA', 'RECUSADA');

-- Create RangeFuncionarios enum type
CREATE TYPE rangefuncionarios_enum AS ENUM ('PEQUENO', 'MEDIO', 'GRANDE');

-- Create Conta table (base for inheritance)
CREATE TABLE conta (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(250) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    nome VARCHAR(250) NOT NULL,
    endereco_id UUID NOT NULL REFERENCES endereco(id) ON DELETE CASCADE,
    role role_enum NOT NULL
);

-- Create Candidato table (inherits from Conta)
CREATE TABLE candidato (
    conta_id UUID PRIMARY KEY REFERENCES conta(id) ON DELETE CASCADE,
    cpf VARCHAR(11) NOT NULL,
    bio VARCHAR(250),
    foto_perfil TEXT,
    tipo_deficiencia tipodeficiencia_enum NOT NULL
);

-- Create Empresa table (inherits from Conta)
CREATE TABLE empresa (
    conta_id UUID PRIMARY KEY REFERENCES conta(id) ON DELETE CASCADE,
    cnpj VARCHAR(250) NOT NULL,
    descricao VARCHAR(250),
    foto_perfil TEXT,
    range_funcionarios rangefuncionarios_enum NOT NULL
);

-- Create Habilidade table
CREATE TABLE habilidade (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(250) NOT NULL,
    anos_experiencia INTEGER NOT NULL,
    candidato_id UUID NOT NULL REFERENCES candidato(conta_id) ON DELETE CASCADE
);

-- Create Contato table
CREATE TABLE contato (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    numero_telefone VARCHAR(20) NOT NULL,
    conta_id UUID NOT NULL REFERENCES conta(id) ON DELETE CASCADE
);

-- Create Vaga table
CREATE TABLE vaga (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    empresa_id UUID NOT NULL REFERENCES empresa(conta_id) ON DELETE CASCADE,
    nome_cargo VARCHAR(250) NOT NULL,
    descricao VARCHAR(250),
    logo_empresa TEXT,
    status_vaga BOOLEAN NOT NULL,
    data_fim_candidatura DATE NOT NULL,
    data_fim_ultima_etapa DATE NOT NULL
);

-- Create Vaga Tags table
CREATE TABLE vaga_tags (
    vaga_id UUID REFERENCES vaga(id) ON DELETE CASCADE,
    tag VARCHAR(250),
    PRIMARY KEY (vaga_id, tag)
);

-- Create Candidatura table
CREATE TABLE candidatura (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidato_id UUID NOT NULL REFERENCES candidato(conta_id) ON DELETE CASCADE,
    vaga_id UUID NOT NULL REFERENCES vaga(id) ON DELETE CASCADE,
    status_candidatura statuscandidatura_enum NOT NULL,
    data_criacao TIMESTAMP NOT NULL
);

ALTER TABLE candidato DROP IF EXISTS id;
ALTER TABLE empresa DROP IF EXISTS id;

