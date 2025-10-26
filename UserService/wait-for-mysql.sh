#!/bin/bash
# wait-for-mysql.sh, only ends when MySQL is reachable. Added to avoid connection errors when starting dependent services.

# Exit immediately if a command exits with a non-zero status
set -e

# Default values (override with environment variables)
host="${MYSQL_HOST:-mysql}"
user="${MYSQL_USER:-springuser}"
password="${MYSQL_PASSWORD:-SpringPass@123}"

echo "Waiting for MySQL at $host with user $user..."

# Loop until MySQL is ready
until mysql -h "$host" -u"$user" -p"$password" -e "SELECT 1;" >/dev/null 2>&1; do
    echo "MySQL is unavailable - sleeping"
    sleep 2
done

until mysql -h "$host" -u"$user" -p"$password" -e "SHOW TABLES;" pipdb >/dev/null 2>&1; do
  echo "Database not ready yet - sleeping"
  sleep 2
done

echo "MySQL is up! Proceeding..."

