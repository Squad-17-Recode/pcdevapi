CREATE TABLE endereco (
    id UUID PRIMARY KEY,
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

CREATE TABLE candidato (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(250) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    nome VARCHAR(250) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    bio VARCHAR(250),
    endereco UUID NOT NULL,
    tipo_deficiencia VARCHAR(50) NOT NULL,
    CONSTRAINT fk_candidato_endereco FOREIGN KEY (endereco) REFERENCES endereco(id)
);

CREATE TABLE empresa (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    nome VARCHAR(250) NOT NULL,
    cnpj VARCHAR(250) NOT NULL,
    descricao VARCHAR(250),
    foto_perfil TEXT,
    endereco_id UUID NOT NULL,
    CONSTRAINT fk_empresa_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

CREATE TABLE habilidade (
    id UUID PRIMARY KEY,
    nome VARCHAR(250) NOT NULL,
    anos_experiencia INTEGER NOT NULL
);

CREATE TABLE contato (
    id UUID PRIMARY KEY,
    numero_telefone VARCHAR(20) NOT NULL
);

CREATE TABLE vaga (
    id UUID PRIMARY KEY,
    empresa_id UUID NOT NULL,
    nome_cargo VARCHAR(250) NOT NULL,
    descricao VARCHAR(250),
    logo_empresa TEXT,
    status_vaga BOOLEAN NOT NULL,
    data_fim_candidatura DATE NOT NULL,
    data_fim_ultima_etapa DATE NOT NULL,
    CONSTRAINT fk_vaga_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE TABLE vaga_tags (
    vaga_id UUID NOT NULL,
    tag VARCHAR(255),
    CONSTRAINT fk_vaga_tags_vaga FOREIGN KEY (vaga_id) REFERENCES vaga(id)
);

CREATE TABLE candidato_habilidades (
    candidato_id UUID NOT NULL,
    habilidade VARCHAR(255),
    CONSTRAINT fk_candidato_habilidades_candidato FOREIGN KEY (candidato_id) REFERENCES candidato(id)
);

CREATE TABLE candidato_contatos (
    candidato_id UUID NOT NULL,
    contato VARCHAR(255),
    CONSTRAINT fk_candidato_contatos_candidato FOREIGN KEY (candidato_id) REFERENCES candidato(id)
);

CREATE TABLE candidatura (
    id UUID PRIMARY KEY,
    candidato_id UUID NOT NULL,
    vaga_id UUID NOT NULL,
    status_candidatura BOOLEAN NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    CONSTRAINT fk_candidatura_candidato FOREIGN KEY (candidato_id) REFERENCES candidato(id),
    CONSTRAINT fk_candidatura_vaga FOREIGN KEY (vaga_id) REFERENCES vaga(id)
);
