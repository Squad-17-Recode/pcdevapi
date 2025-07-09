-- V3: Refatora para um modelo de Conta centralizado e remove colunas redundantes.
-- Este script centraliza as informações de login e endereço na nova tabela 'conta'.

-- 1. Criar a tabela 'conta' para centralizar os dados comuns, incluindo o campo 'nome'.
CREATE TABLE conta (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(250) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    nome VARCHAR(250) NOT NULL,
    endereco_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('CANDIDATO', 'EMPRESA')), -- Coluna para diferenciar os tipos de conta
    CONSTRAINT fk_conta_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

-- 2. Migrar a tabela 'candidato' para usar a herança JOINED.
-- Adiciona a coluna 'conta_id' que será a nova PK/FK.
ALTER TABLE candidato ADD COLUMN conta_id UUID;
-- (Em um cenário real com dados, aqui ocorreria a migração de dados das colunas antigas para a tabela 'conta')
ALTER TABLE candidato DROP CONSTRAINT fk_candidato_endereco;
ALTER TABLE candidato DROP COLUMN username, DROP COLUMN email, DROP COLUMN senha, DROP COLUMN nome, DROP COLUMN endereco;
ALTER TABLE candidato DROP CONSTRAINT candidato_pkey;
ALTER TABLE candidato DROP COLUMN id;
ALTER TABLE candidato ALTER COLUMN conta_id SET NOT NULL;
ALTER TABLE candidato ADD PRIMARY KEY (conta_id);
ALTER TABLE candidato ADD CONSTRAINT fk_candidato_conta FOREIGN KEY (conta_id) REFERENCES conta(id) ON DELETE CASCADE;

-- 3. Migrar a tabela 'empresa' para usar a herança JOINED.
ALTER TABLE empresa ADD COLUMN conta_id UUID;
ALTER TABLE empresa DROP CONSTRAINT fk_empresa_endereco;
ALTER TABLE empresa DROP COLUMN username, DROP COLUMN email, DROP COLUMN senha, DROP COLUMN nome, DROP COLUMN endereco_id;
ALTER TABLE empresa DROP CONSTRAINT empresa_pkey;
ALTER TABLE empresa DROP COLUMN id;
ALTER TABLE empresa ALTER COLUMN conta_id SET NOT NULL;
ALTER TABLE empresa ADD PRIMARY KEY (conta_id);
ALTER TABLE empresa ADD CONSTRAINT fk_empresa_conta FOREIGN KEY (conta_id) REFERENCES conta(id) ON DELETE CASCADE;