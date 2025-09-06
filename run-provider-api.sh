#!/usr/bin/env bash
set -e  # exit if any command fails

cd provider-api

echo ">>> Building provider-api"
./mvnw clean package -DskipTests

echo ">>> Starting provider-api on port 8089"
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8089"
