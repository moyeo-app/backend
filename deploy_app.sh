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
docker compose --env-file "$ENV_FILE" up -d --no-deps --force-recreate --wait --wait-timeout ${WAIT_TIMEOUT:-1200} backend

# ==== 헬스 체크 ====
WAIT=${WAIT_TIMEOUT:-1200}
SLEPT=0

is_ready_log() {
  local logs
  logs=$(docker compose --env-file "$ENV_FILE" logs --since 30s backend 2>/dev/null || true)
  if echo "$logs" | grep -q 'APP_READY'; then
    return 0
  fi
  if echo "$logs" | grep -q 'Tomcat started on port' && \
     echo "$logs" | grep -q 'DispatcherServlet - Completed initialization'; then
    return 0
  fi
  return 1
}

echo "[deploy] waiting app readiness (timeout=${WAIT}s)"
STEP=2
while (( SLEPT < WAIT )); do
  if is_ready_log; then
    echo "[deploy] READY ✅"
    echo "Deploy OK"
    exit 0
  fi
  # 부팅 실패 빠른 감지
  if docker compose --env-file "$ENV_FILE" logs --since 30s backend 2>/dev/null | grep -q "Application run failed"; then
    echo "[deploy] Spring Boot failed to start. Last logs:";
    docker compose --env-file "$ENV_FILE" logs --tail=200 backend || true
    exit 1
  fi
  sleep "$STEP"; SLEPT=$((SLEPT + STEP)); (( STEP<10 )) && STEP=$((STEP+1))
  echo "[deploy] not ready yet... ${SLEPT}s elapsed"
done

echo "[deploy] timeout. last logs:"
docker logs --tail=200 moyeo-backend || true
exit 1
