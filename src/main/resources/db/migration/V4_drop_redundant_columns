-- Remove a restrição de chave estrangeira da tabela 'candidato' antes de remover a coluna.
ALTER TABLE candidato DROP CONSTRAINT IF EXISTS fk_candidato_endereco;

-- Remove as colunas especificadas da tabela 'candidato'.
ALTER TABLE candidato
    DROP COLUMN username,
    DROP COLUMN email,
    DROP COLUMN senha,
    DROP COLUMN nome,
    DROP COLUMN endereco,
    DROP COLUMN conta_id;

-- Remove a restrição de chave estrangeira da tabela 'empresa' antes de remover a coluna.
ALTER TABLE empresa DROP CONSTRAINT IF EXISTS fk_empresa_endereco;

-- Remove as colunas especificadas da tabela 'empresa'.
ALTER TABLE empresa
    DROP COLUMN username,
    DROP COLUMN email,
    DROP COLUMN senha,
    DROP COLUMN nome,
    DROP COLUMN endereco_id,
    DROP COLUMN conta_id;
