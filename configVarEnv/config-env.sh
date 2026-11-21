#!/usr/bin/env bash

DEFAULT_DB_URL="jdbc:mysql://localhost:3306/porElGusto"
DEFAULT_DB_USERNAME="root"
DEFAULT_SECRET="dev-only-change-this-token-secret-32-bytes-minimum"

read -r -p "URL de BD [$DEFAULT_DB_URL]: " input_url
read -r -p "Usuario de BD [$DEFAULT_DB_USERNAME]: " input_user
read -r -s -p "Contrasena de BD: " input_pass
echo ""
read -r -p "Secreto auth (min 32 chars) [$DEFAULT_SECRET]: " input_secret

export DB_URL="${input_url:-$DEFAULT_DB_URL}"
export DB_USERNAME="${input_user:-$DEFAULT_DB_USERNAME}"
export DB_PASSWORD="$input_pass"
export AUTH_TOKEN_SECRET="${input_secret:-$DEFAULT_SECRET}"
export SPRING_PROFILES_ACTIVE="prod"

echo "Variables configuradas en esta sesion (SPRING_PROFILES_ACTIVE=prod)."