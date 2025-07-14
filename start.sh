#!/bin/bash

# Build and run script for pcdevapi

set -e

echo "🐳 Building pcdevapi Docker container..."

# Check if .env file exists
if [ ! -f .env ]; then
    echo "⚠️  .env file not found. Copying from .env.example..."
    cp .env.example .env
    echo "✅ Created .env file. Please review and update the values if needed."
fi

# Build the Docker image
echo "🔨 Building Docker image..."
docker compose build

# Start the services
echo "🚀 Starting services..."
docker compose up -d

# Show logs
echo "📋 Showing logs..."
docker compose logs -f pcdevapi
