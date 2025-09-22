# 모여모여: 챌린지 참여 및 인증 관리 플랫폼

## 📌 서비스 소개

- 스터디 챌린지 참여와 인증 과정을 관리할 수 있는 플랫폼
- 사용자는 원하는 챌린지에 참여하고, 인증을 통해 학습을 기록할 수 있으며, 자동화된 통계와 AI 피드백으로 성장 과정을 확인할 수 있습니다.

## 🎯 서비스 목표

- **공동 학습 동기 부여**: 혼자가 아닌 함께하는 챌린지를 통해 지속 가능한 학습 습관 형성
- **안정적인 데이처 관리**: 선착순, 대규모 환경에서도 안정적으로 데이터 처리


## 🔑 주요 기능

1. **회원 관리**
    - OAuth2 소셜 로그인 (Google, Kakao)
    - JWT 기반 인증/인가 처리
2. **챌린지 기능**
    - 챌린지 생성
    - 챌린지 선착순 참여
    - 챌린지 내용 인증
3. **통계 및 리포트**
    - 학습 로그 자동 기록 및 주간 통계 집계
    - AI 기반 맞춤형 피드백 제공 (Gemini 연동)
4. **결제 기능**
    - 챌린지 참여 결제 관리
5. **모니터링 및 운영**
    - Prometheus + Grafana + Loki 기반 대시보드 구축
    - Kafka-UI, Redis-Exporter 등 운영 툴 제공
    - GitHub Actions 기반 CI/CD 자동 배포


## ⚙️ 기술 스택

### Backend

- **Java 17**, **Spring Boot 3.5.x**
- Spring Security, Spring Data JPA, Spring Batch
- OAuth2, JWT

### Database

- **PostgreSQL**
- **Redis** 

### Message Queue

- **Apache Kafka**

### DevOps & Infra

- **AWS EC2, RDS**
- **Docker Compose** 
- **Prometheus, Grafana, Loki** 
- GitHub Actions 

### Tools

- GitHub 
- JIRA, Confluence

## 인프라 아키텍쳐
<img width="16384" height="10303" alt="KakaoTalk_Photo_2025-09-22-21-26-06" src="https://github.com/user-attachments/assets/9aa8bb6b-e39e-4844-afe0-aca8667d48ff" />

## ERD
<img width="1472" height="1076" alt="KakaoTalk_Photo_2025-09-22-21-26-14" src="https://github.com/user-attachments/assets/4cf69dad-1dd7-4487-ab3b-153d3b133410" />

