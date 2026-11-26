#!/usr/bin/env bash
# Descarga la base gratuita "IP to City Lite" de DB-IP (licencia CC BY 4.0, sin cuenta ni llave)
# para ver país y ciudad en las estadísticas del panel. Se actualiza cada mes: vuelve a ejecutarlo cuando quieras.
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p geoip
mes=$(date +%Y-%m)
url="https://download.db-ip.com/free/dbip-city-lite-${mes}.mmdb.gz"
echo "Descargando ${url}"
if ! curl -fsSL -o geoip/dbip-city-lite.mmdb.gz "$url"; then
  mes=$(date -d "$(date +%Y-%m-01) -1 month" +%Y-%m)
  url="https://download.db-ip.com/free/dbip-city-lite-${mes}.mmdb.gz"
  echo "No está la del mes actual; descargando ${url}"
  curl -fsSL -o geoip/dbip-city-lite.mmdb.gz "$url"
fi
gunzip -f geoip/dbip-city-lite.mmdb.gz
echo "Listo: geoip/dbip-city-lite.mmdb (reinicia la aplicación)"
