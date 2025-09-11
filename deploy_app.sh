set -euo pipefail

APP_DIR="/opt/moyeo/app"
ENV_FILE="$APP_DIR/.env.app.prod"
COMPOSE_FILE="$APP_DIR/docker-compose.yml"

# env, docker 파일 없는 경우
if [ ! -f "$ENV_FILE" ] || [ ! -f "$COMPOSE_FILE" ]; then
  echo "Missing $ENV_FILE or $COMPOSE_FILE" >&2
  exit 1
fi

cd "$APP_DIR"

# 백업
TS=$(date +%Y%m%d%H%M%S)
if [ -f app.jar ]; then
  cp -f app.jar "app-$TS.jar";
fi

# CI가 /tmp/app.jar 로 올려둠
mv /tmp/app.jar "$APP_DIR/app.jar"

# 백엔드만 재기동
docker compose --env-file "$ENV_FILE" up -d --no-deps --force-recreate backend

# ==== 헬스 체크 (retry with backoff) ====
HEALTH_URL=${HEALTH_URL:-http://localhost:8080/actuator/health}
MAX_TRIES=${MAX_TRIES:-15}   # 총 시도 횟수(기본 15회)
SLEEP_SECS=${SLEEP_SECS:-2}  # 각 시도 간 대기(기본 2초)

echo "Waiting for health: $HEALTH_URL"
ok=0
for i in $(seq 1 "$MAX_TRIES"); do
  if curl -fsS -m 5 "$HEALTH_URL" | jq -e '.status=="UP"' >/dev/null; then
    echo "Health is UP"
    ok=1
    break
  fi
  echo "[$i/$MAX_TRIES] not ready yet... retrying in ${SLEEP_SECS}s"
  sleep "$SLEEP_SECS"
done

if [[ "${ok:-0}" -ne 1 ]]; then
  echo "Health check failed; printing last 100 lines:"
  docker logs --tail=100 moyeo-backend || true
  exit 1
fi
# ==== 헬스 체크 끝 ====

echo "Deploy OK"
