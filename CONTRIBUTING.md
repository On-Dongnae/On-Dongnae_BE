# 🤝 On-Dongnae 백엔드 협업 가이드

> **팀원 모두가 반드시 읽고 숙지해야 하는 문서입니다.**  
> 이 가이드를 따르지 않으면 코드 충돌, 데이터 손실, 서비스 장애가 발생할 수 있습니다.

---

## 📌 목차

1. [개발 환경 세팅 (처음 1회)](#1-개발-환경-세팅-처음-1회)
2. [로컬 서버 실행 방법](#2-로컬-서버-실행-방법)
3. [Git 브랜치 전략](#3-git-브랜치-전략)
4. [커밋 메시지 규칙](#4-커밋-메시지-규칙)
5. [개발 플로우 (처음부터 끝까지)](#5-개발-플로우-처음부터-끝까지)
6. [자주 하는 실수 & 해결법](#6-자주-하는-실수--해결법)

---

## 1. 개발 환경 세팅 (처음 1회)

### 필수 설치 프로그램

| 프로그램 | 버전 | 설치 링크 | 용도 |
|---------|------|----------|------|
| **JDK** | 17 이상 | [Adoptium](https://adoptium.net/) | Java 컴파일 및 실행 |
| **IntelliJ IDEA** | Community 이상 | [JetBrains](https://www.jetbrains.com/idea/) | IDE (코드 편집기) |
| **Docker Desktop** | 최신 | [Docker](https://www.docker.com/products/docker-desktop/) | 데이터베이스(PostgreSQL, Redis) 실행 |
| **Git** | 최신 | [Git](https://git-scm.com/) | 버전 관리 |

### 프로젝트 클론

```bash
# 1. 원하는 폴더에서 프로젝트 다운로드
git clone https://github.com/On-Dongnae/On-Dongnae_BE.git

# 2. 프로젝트 폴더로 이동
cd On-Dongnae_BE
```

### 환경 변수 파일 생성

프로젝트 루트에 `.env` 파일이 필요합니다. **이 파일은 Git에 올라가지 않으므로** 직접 생성해야 합니다.

프로젝트 루트(`On-Dongnae_BE/`)에 `.env` 파일을 만들고 아래 내용을 붙여넣으세요:

```properties
# --- PostgreSQL ---
POSTGRES_DB=on_dongnae
POSTGRES_USER=odn_user
POSTGRES_PASSWORD=odn_pass
DB_URL=jdbc:postgresql://localhost:5432/on_dongnae
DB_USERNAME=odn_user
DB_PASSWORD=odn_pass

# --- Redis ---
REDIS_HOST=localhost
REDIS_PORT=6379
```

> ⚠️ **주의**: `.env` 파일에는 비밀번호 등 민감 정보가 포함되므로 **절대 GitHub에 push하지 마세요.** `.gitignore`에 이미 등록되어 있습니다.

---

## 2. 로컬 서버 실행 방법

### Step 1: Docker Desktop 실행

Windows 작업 표시줄에서 **Docker Desktop** 앱을 실행합니다.  
(왼쪽 하단에 "Engine running" 표시가 나올 때까지 기다리세요)

### Step 2: 데이터베이스 컨테이너 실행

터미널(PowerShell 또는 Git Bash)에서 **프로젝트 루트 폴더**로 이동 후:

```bash
docker compose up -d
```

이 명령어는 다음을 자동으로 수행합니다:
- PostgreSQL 16 데이터베이스 실행 (포트 5432)
- Redis 7 캐시 서버 실행 (포트 6379)

### Step 3: 컨테이너가 정상 실행 중인지 확인

```bash
docker compose ps
```

아래처럼 `Up`과 `healthy` 상태가 보이면 성공입니다:

```
NAME           IMAGE            STATUS
odn-postgres   postgres:16      Up (healthy)
odn-redis      redis:7-alpine   Up (healthy)
```

### Step 4: Spring Boot 애플리케이션 실행

**IntelliJ**에서:
1. `OnDongnaeApplication.java` 파일을 열고
2. `main` 메서드 옆의 ▶ (초록 재생 버튼)을 클릭

또는 터미널에서:

```bash
./gradlew bootRun
```

### Step 5: 정상 실행 확인

콘솔에 아래 메시지가 보이면 서버가 정상 동작 중입니다:

```
Started OnDongnaeApplication in X.XXX seconds
Tomcat started on port 8080
```

브라우저에서 `http://localhost:8080` 접속 가능합니다.

### 서버 종료 시

```bash
# Spring Boot: IntelliJ에서 빨간 ■ 정지 버튼 클릭 또는 Ctrl+C

# Docker 컨테이너 종료 (DB 데이터는 유지됨)
docker compose down

# Docker 컨테이너 + DB 데이터까지 완전 삭제 (초기화)
docker compose down -v
```

---

## 3. Git 브랜치 전략

### 🚨 절대 금지: main 브랜치에 직접 push

```
❌ 절대로 이렇게 하지 마세요
git checkout main
(코드 수정 작업)
git add .
git commit -m "feat: 무언가 추가"
git push   ← main에 바로 push됨. 위험!!!
```

**왜 위험한가?**

| 문제 | 설명 |
|------|------|
| **코드 충돌** | 두 사람이 같은 파일을 수정하면 서로의 코드를 덮어쓸 수 있음 |
| **버그 전파** | 테스트하지 않은 코드가 바로 main에 합쳐져서 다른 사람의 개발 환경을 망가뜨림 |
| **되돌리기 불가** | 문제가 생겼을 때 어떤 커밋이 원인인지 찾기 어려움 |
| **코드 리뷰 불가** | 코드를 검토할 기회 없이 바로 반영되므로 품질 저하 |

### ✅ 올바른 브랜치 구조

```
main (배포용, 안정 버전)
 └── develop (개발 통합, 기능을 모아두는 곳)
      ├── feature/user-login    (로그인 기능 개발)
      ├── feature/feed-crud     (피드 CRUD 개발)
      ├── feature/mission-api   (미션 API 개발)
      └── fix/score-bug         (버그 수정)
```

| 브랜치 | 용도 | 누가 push 가능? |
|--------|------|-----------------|
| `main` | 최종 배포 버전 | **아무도 직접 push 금지**. PR만 허용 |
| `develop` | 개발 중인 기능 통합 | feature 브랜치에서 PR로만 병합 |
| `feature/*` | 새 기능 개발 | 각자 자유롭게 push |
| `fix/*` | 버그 수정 | 각자 자유롭게 push |

### 브랜치 이름 규칙

```
feature/기능이름    → 새로운 기능 개발
fix/버그이름        → 버그 수정
refactor/대상이름   → 리팩토링 (기능 변경 없는 코드 개선)
docs/문서이름       → 문서 작성/수정
chore/설정이름      → 설정, 환경, 빌드 관련 작업
```

**예시:**

```bash
feature/user-login
feature/feed-crud
feature/mission-verification
fix/score-calculation-error
refactor/user-service
docs/api-specification
chore/docker-setup
```

---

## 4. 커밋 메시지 규칙

### 형식

```
<타입>: <제목>
```

### 타입 목록

| 타입 | 사용 시점 | 예시 |
|------|----------|------|
| `feat` | 새로운 기능 추가 | `feat: 로그인 API 구현` |
| `fix` | 버그 수정 | `fix: 점수 계산 오류 수정` |
| `refactor` | 코드 리팩토링 (기능 변경 X) | `refactor: UserService 메서드 분리` |
| `docs` | 문서 추가/수정 | `docs: API 명세 작성` |
| `chore` | 설정, 환경, 빌드 관련 | `chore: Docker Compose 설정 추가` |
| `style` | 코드 포맷, 세미콜론 등 (로직 변경 X) | `style: 코드 포맷팅 적용` |
| `test` | 테스트 코드 추가/수정 | `test: UserService 단위 테스트 작성` |

### 규칙

1. **한글로 작성**합니다 (우리 팀은 한국어 사용)
2. **제목은 간결하게** 50자 이내로 작성합니다
3. **마침표(.)를 붙이지 않습니다**
4. **무엇을 했는지** 명확히 적습니다

### 좋은 예 vs 나쁜 예

```bash
# ✅ 좋은 예
feat: 회원가입 API 구현
fix: 피드 삭제 시 이미지 미삭제 버그 수정
refactor: MissionService 검증 로직 분리
chore: Redis 의존성 추가

# ❌ 나쁜 예
수정함                    → 타입이 없고, 무엇을 수정했는지 불명확
feat: 완료.               → 마침표 불필요, 내용이 불명확
fix: 버그 수정             → 무슨 버그인지 모름
ㅋㅋ                       → ...
```

---

## 5. 개발 플로우 (처음부터 끝까지)

### 내가 "피드 CRUD 기능"을 개발해야 한다면:

```bash
# 1. 최신 develop 브랜치로 이동 & 최신 코드 받기
git checkout develop
git pull origin develop

# 2. 새로운 feature 브랜치 생성
git checkout -b feature/feed-crud

# 3. 코드 작성... (이 브랜치에서 마음껏 개발)
#    FeedController.java 작성
#    FeedService.java 작성
#    ...

# 4. 작업한 파일들을 스테이징 (Git에 등록)
git add .

# 5. 커밋 (의미 있는 단위로 나눠서!)
git commit -m "feat: 피드 작성 API 구현"

# 6. 추가 작업...
git add .
git commit -m "feat: 피드 수정 및 삭제 API 구현"

# 7. 원격 저장소에 내 브랜치 올리기
git push origin feature/feed-crud

# 8. GitHub에서 Pull Request(PR) 생성
#    → base: develop  ←  compare: feature/feed-crud
#    → 팀원에게 코드 리뷰 요청

# 9. 리뷰 완료 후 develop에 병합 (GitHub에서 Merge 버튼)

# 10. 로컬 정리
git checkout develop
git pull origin develop
git branch -d feature/feed-crud   # 완료된 브랜치 삭제
```

### 핵심 요약 (플로우 다이어그램)

```
develop에서 최신 pull
    ↓
feature 브랜치 생성
    ↓
코드 작성 & 커밋 (여러 번 가능)
    ↓
push → GitHub PR 생성
    ↓
코드 리뷰
    ↓
develop에 merge
    ↓
feature 브랜치 삭제
```

---

## 6. 자주 하는 실수 & 해결법

### 실수 1: 브랜치를 안 만들고 main에서 작업해버림

```bash
# 아직 커밋을 안 한 경우 → 브랜치만 만들면 됨
git checkout -b feature/내기능

# 이미 main에 커밋까지 해버린 경우 → 커밋을 옮기기
git checkout -b feature/내기능          # 현재 커밋을 가진 채로 브랜치 생성
git checkout main                       # 다시 main으로 이동
git reset --hard origin/main            # main을 원격 상태로 되돌림
git checkout feature/내기능             # 다시 feature 브랜치로 이동 (내 코드가 살아있음)
```

### 실수 2: Docker 컨테이너가 안 뜸

```bash
# Docker Desktop이 실행 중인지 확인
docker info

# "Cannot connect" 에러가 나면 → Docker Desktop 앱을 켜세요

# 포트 충돌 시 (5432 또는 6379가 이미 사용 중)
# 로컬에 이미 PostgreSQL이나 Redis가 설치되어 있으면 중지하세요
```

### 실수 3: 서버 실행 시 DB 연결 에러

```
password authentication failed for user "odn_user"
```

→ `.env` 파일의 비밀번호와 Docker의 비밀번호가 불일치합니다.  
→ 컨테이너를 완전 삭제 후 재생성하세요:

```bash
docker compose down -v     # 볼륨까지 삭제 (비밀번호 초기화)
docker compose up -d       # 새로운 비밀번호로 재생성
```

### 실수 4: `git pull` 했는데 충돌(conflict) 발생

```bash
# 1. 충돌난 파일을 열어서 <<<<<< 부분을 수동으로 정리
# 2. 정리 완료 후
git add .
git commit -m "fix: 머지 충돌 해결"
```

> 💡 **충돌이 무섭다면?** 작업 전에 항상 `git pull origin develop`을 습관화하세요. 자주 pull할수록 충돌이 줄어듭니다.

---

## 📋 체크리스트 (매일 개발 시작 전)

```
[ ] Docker Desktop 실행됨
[ ] docker compose ps → postgres, redis 모두 healthy
[ ] git checkout develop → git pull origin develop (최신 코드 받기)
[ ] feature/내기능 브랜치에서 작업 중인지 확인 (git branch 명령어로)
```
