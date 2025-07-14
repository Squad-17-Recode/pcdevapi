# Environment Configuration Guide

## 🔒 Security Notice
**NEVER commit `.env` files with real credentials to version control!**

## 🚀 Setup Instructions

### 1. Copy the example file
```bash
cp .env.example .env
```

### 2. Edit `.env` with your real values
```bash
# Database Configuration - Replace with your actual database credentials
DATABASE_URL=jdbc:postgresql://your-database-host:port/database_name?ssl=require&user=username&password=password
DATABASE_USERNAME=your_actual_username
DATABASE_PASSWORD=your_actual_password

# JWT Configuration - Generate a secure random key
JWT_SECRET=your_secure_jwt_secret_key_here_minimum_256_bits
JWT_EXPIRATION=43200000

# Server Configuration
SERVER_PORT=8080
```

### 3. Generate a secure JWT secret
You can generate a secure JWT secret using:
```bash
# Option 1: Using openssl (recommended)
openssl rand -base64 32

# Option 2: Using Node.js
node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"

# Option 3: Online generator (use only for development)
# https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx
```

## 🔐 Security Best Practices

### For Development:
1. Keep `.env` files local only
2. Use strong, unique JWT secrets
3. Don't share credentials via chat/email

### For Production:
1. Use environment variables or secrets management (Azure Key Vault, AWS Secrets Manager, etc.)
2. Rotate secrets regularly
3. Use different credentials for each environment
4. Enable database SSL/TLS

## 🐳 Docker Environment

The Docker setup automatically loads variables from `.env` file:
- Variables are passed to the container via `env_file: - .env`
- Container internal port is always 8080
- External port can be configured via `SERVER_PORT` variable

## 🔧 Troubleshooting

### Container won't start?
1. Check if `.env` file exists: `ls -la .env`
2. Verify credentials are correct
3. Test database connectivity
4. Check Docker logs: `docker compose logs pcdevapi`

### Need different environment?
Create separate files like `.env.development`, `.env.staging`, etc.
Then specify in docker-compose:
```yaml
env_file:
  - .env.production  # or .env.staging
```
