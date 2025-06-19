-- Set all existing NULL endereco values to a valid endereco id (replace 'YOUR_EXISTING_ENDERECO_ID' with a real id)
-- UPDATE candidato SET endereco = 'YOUR_EXISTING_ENDERECO_ID' WHERE endereco IS NULL;

-- Now set the column as NOT NULL
ALTER TABLE candidato ALTER COLUMN endereco SET NOT NULL;
