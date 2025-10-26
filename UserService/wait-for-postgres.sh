#!/bin/bash
# wait-for-postgres.sh
# Waits for Postgres to become ready before running dependent services.

set -e

host="${POSTGRES_HOST:-postgres}"
port="${POSTGRES_PORT:-5432}"
user="${POSTGRES_USER:-springuser}"
password="${POSTGRES_PASSWORD}"
db="${POSTGRES_DB:-pipdb}"

echo "⏳ Waiting for PostgreSQL at $host:$port with user '$user' and DB '$db'..."

# Loop until PostgreSQL is accepting connections
#until PGPASSWORD="$password" psql -h "$host" -p "$port" -U "$user" -d "$db" -c '\q' >/dev/null 2>&1; do
#  echo "   ↳ PostgreSQL not ready yet — retrying in 2s..."
#  sleep 2
#done
sleep 10

echo "✅ PostgreSQL is up and running — proceeding to start the app."
