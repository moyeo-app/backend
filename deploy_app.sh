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

# ==== 헬스 체크 ====
echo "[deploy] waiting for backend health..."
for i in $(seq 1 120); do
  status=$(docker inspect -f '{{.State.Health.Status}}' moyeo-backend 2>/dev/null || echo starting)
  if [ "$status" = "healthy" ]; then
    echo "[deploy] backend healthy ✅"
    exit 0
  fi
  if [ "$status" = "unhealthy" ]; then
    echo "[deploy] backend unhealthy (try $i) ❌"
  else
    echo "[deploy] backend status: $status (try $i)"
  fi
  sleep 5
done

if [[ "${ok:-0}" -ne 1 ]]; then
  echo "Health check failed; printing last 100 lines:"
  docker logs --tail=100 moyeo-backend || true
  exit 1
fi
# ==== 헬스 체크 끝 ====

echo "Deploy OK"
