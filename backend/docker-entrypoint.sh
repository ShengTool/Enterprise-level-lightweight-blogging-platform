#!/bin/sh
set -e

# Set default values for environment variables
DB_HOST=${DB_HOST:-mysql}
DB_PORT=${DB_PORT:-3306}
MAX_RETRIES=${MAX_RETRIES:-30}
RETRY_INTERVAL=${RETRY_INTERVAL:-2}

# Validate required environment variables
if [ -z "$DB_USERNAME" ] || [ -z "$DB_PASSWORD" ]; then
    echo "ERROR: DB_USERNAME and DB_PASSWORD environment variables are required"
    exit 1
fi

if [ -z "$JWT_SECRET" ]; then
    echo "WARNING: JWT_SECRET environment variable is not set. Using default development secret."
    echo "WARNING: In production, set a strong JWT_SECRET (at least 32 characters)"
fi

# Wait for MySQL to be ready
echo "Waiting for MySQL at $DB_HOST:$DB_PORT..."
retry_count=0
while ! nc -z "$DB_HOST" "$DB_PORT"; do
    retry_count=$((retry_count + 1))
    if [ $retry_count -ge $MAX_RETRIES ]; then
        echo "ERROR: MySQL is not ready after $MAX_RETRIES retries"
        exit 1
    fi
    echo "MySQL not ready yet. Retry $retry_count/$MAX_RETRIES..."
    sleep $RETRY_INTERVAL
done

echo "MySQL is ready!"

# Execute the main command
exec "$@"
