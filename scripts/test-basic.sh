#!/usr/bin/env bash

set -euo pipefail

API_GATEWAY_URL="${API_GATEWAY_URL:-http://localhost:8080}"
TRACKING_URL="${TRACKING_URL:-http://localhost:8086}"

echo "Waiting for API gateway..."
until [ "$(curl -s -o /dev/null -w '%{http_code}' "${API_GATEWAY_URL}/api/users/does-not-exist" || true)" != "000" ]; do
  sleep 3
done

echo "Creating base..."
BASE_RESPONSE="$(curl -fsS -X POST "${API_GATEWAY_URL}/api/drones/base?name=ParisBase&lat=48.8566&lon=2.3522&capacity=5")"
echo "${BASE_RESPONSE}"
BASE_ID="$(echo "${BASE_RESPONSE}" | sed -n 's/.*"name":"\([^"]*\)".*/\1/p')"

echo "Creating drone..."
DRONE_RESPONSE="$(curl -fsS -X POST "${API_GATEWAY_URL}/api/drones/drone?baseId=${BASE_ID}")"
echo "${DRONE_RESPONSE}"

USER_EMAIL="john.$(date +%s)@example.com"
echo "Registering user ${USER_EMAIL}..."
USER_RESPONSE="$(curl -fsS -X POST "${API_GATEWAY_URL}/api/users/register?name=John&email=${USER_EMAIL}&password=secret123")"
echo "${USER_RESPONSE}"
USER_ID="$(echo "${USER_RESPONSE}" | sed -n 's/.*"id":"\([^"]*\)".*/\1/p')"

NOW="$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
LATER="$(date -u -d '+1 hour' +"%Y-%m-%dT%H:%M:%SZ" 2>/dev/null || date -u -v+1H +"%Y-%m-%dT%H:%M:%SZ")"

echo "Creating delivery..."
DELIVERY_RESPONSE="$(curl -fsS -X POST "${API_GATEWAY_URL}/api/deliveries" \
  -H 'Content-Type: application/json' \
  -d "{
    \"userId\":\"${USER_ID}\",
    \"pickupLocationLat\":48.8566,
    \"pickupLocationLon\":2.3522,
    \"dropoffLocationLat\":48.8666,
    \"dropoffLocationLon\":2.3722,
    \"weight\":1.5,
    \"requestedTimeStart\":\"${NOW}\",
    \"requestedTimeEnd\":\"${LATER}\"
  }")"
echo "${DELIVERY_RESPONSE}"
DELIVERY_ID="$(printf '%s' "${DELIVERY_RESPONSE}" | tr -d '"')"

echo "Starting delivery ${DELIVERY_ID}..."
curl -fsS -X POST "${API_GATEWAY_URL}/api/deliveries/${DELIVERY_ID}/start"
echo

echo "Delivery status:"
curl -fsS "${API_GATEWAY_URL}/api/deliveries/${DELIVERY_ID}/status"
echo

echo "Delivery remaining time:"
curl -fsS "${API_GATEWAY_URL}/api/deliveries/${DELIVERY_ID}/remaining-time"
echo

echo "Tracking snapshot:"
curl -fsS "${TRACKING_URL}/tracking/${DELIVERY_ID}" || true
echo
