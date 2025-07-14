# PCDev API - Docker Setup

## 🔒 **SECURITY FIRST - READ THIS!**
**⚠️ NEVER commit `.env` files with real credentials to Git!**
**📖 Read `ENVIRONMENT.md` for complete security guidelines**

## Prerequisites

- Docker
- Docker Compose
- curl (for health checks)

## Quick Start

1. **Clone the repository and navigate to the API directory:**
   ```bash
   cd pcdevapi
   ```

2. **Set up environment variables (IMPORTANT!):**
   ```bash
   cp .env.example .env
   # CRITICAL: Edit .env file with your REAL credentials
   # The .env.example contains PLACEHOLDER values only!
   # See ENVIRONMENT.md for security guidelines
   ```

3. **Generate a secure JWT secret:**
   ```bash
   # Generate a secure JWT secret (replace the placeholder in .env)
   openssl rand -base64 32
   ```

4. **Build and run:**
   ```bash
   ./start.sh
   ```

   Or manually:
   ```bash
   docker compose up --build
   ```

## ⚠️ Security Configuration

### DO NOT use the example credentials in production!
The `.env.example` file contains placeholder values. You MUST:

1. **Database**: Use your actual database credentials
2. **JWT Secret**: Generate a cryptographically secure random key
3. **Environment isolation**: Use different credentials per environment

### Secure JWT Secret Generation:
```bash
# Method 1: OpenSSL (recommended)
openssl rand -base64 32

# Method 2: Node.js
node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"
```

## Environment Variables

Edit your `.env` file with real values:

- `DATABASE_URL`: Your actual PostgreSQL connection string
- `DATABASE_USERNAME`: Your database username
- `DATABASE_PASSWORD`: Your database password
- `JWT_SECRET`: **Generate a secure random key - DO NOT use the example!**
- `JWT_EXPIRATION`: Token expiration time in milliseconds
- `SERVER_PORT`: Port to expose (default: 8080)

## Available Commands

### Build and start services
```bash
docker compose up --build -d
```

### View logs
```bash
docker compose logs -f pcdevapi
```

### Stop services
```bash
docker compose down
```

### Rebuild from scratch
```bash
docker compose down --volumes
docker compose build --no-cache
docker compose up -d
```

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

## Production Deployment

🔐 **CRITICAL for Production:**

1. **Secrets Management**: Use proper secrets management (Azure Key Vault, AWS Secrets Manager, HashiCorp Vault)
2. **Environment Variables**: Inject secrets via environment variables, not files
3. **Database Security**: Use SSL/TLS, network isolation, strong authentication
4. **JWT Security**: Use HSA256 with strong keys, implement key rotation
5. **Monitoring**: Set up logging, monitoring, and alerting
6. **HTTPS**: Always use HTTPS with proper SSL certificates
7. **Network Security**: Use firewalls, VPNs, and network segmentation

## Troubleshooting

### Container won't start
- Check logs: `docker compose logs pcdevapi`
- Verify `.env` file exists and contains real values
- Test database connectivity
- Ensure port 8080 is available

### Health check fails
- Wait for application startup (can take 60+ seconds)
- Check if port 8080 is accessible
- Verify database connection in logs

### Build fails
- Clear Docker cache: `docker system prune -a`
- Check if all required environment variables are set
- Verify Maven dependencies are accessible

### Security Issues
- **Never commit `.env` files**
- **Always use strong, unique passwords**
- **Rotate secrets regularly**
- **Review logs for security events**

## Files Structure
```
pcdevapi/
├── .env.example          # Template (safe to commit)
├── .env                  # Real credentials (NEVER commit!)
├── .gitignore           # Includes .env exclusion
├── Dockerfile           # Container definition
├── docker-compose.yml   # Service orchestration
├── ENVIRONMENT.md       # Security guidelines
└── README-Docker.md     # This file
```
