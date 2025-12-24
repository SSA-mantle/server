# 싸멘틀(SSAmantle) 프로젝트 종합 문서

## 문서 정보
- **프로젝트명**: 싸멘틀 (ssamantle)
- **최종 수정일**: 2025-12-24
- **작성자**: 길태환

---

## 목차
1. [요구사항 명세](#1-요구사항-명세)
2. [Use Case 다이어그램](#2-use-case-다이어그램)
3. [클래스 다이어그램](#3-클래스-다이어그램)
4. [ERD (Entity Relationship Diagram)](#4-erd-entity-relationship-diagram)
5. [WBS & 간트 차트](#5-wbs--간트-차트)
6. [모듈 구조](#6-모듈-구조)
7. [화면 설계서](#7-화면-설계서)

---

## 1. 요구사항 명세

### 1.1 프로젝트 개요

#### 1.1.1 프로젝트 목적
싸멘틀(SSAmantle)은 **SSAFY(싸피) + Semantle(의미 기반 단어 추론 게임)**을 결합한 단어 추론 게임 플랫폼입니다. 사용자는 매일 제시되는 정답 단어를 의미 유사도를 기반으로 추론하여 맞추는 게임을 즐길 수 있습니다.

#### 1.1.2 핵심 가치
- **학습성**: 단어의 의미적 관계를 이해하며 어휘력 향상
- **경쟁성**: 일일 리더보드를 통한 실력 경쟁
- **지속성**: 연속 풀이 시스템(스트릭)을 통한 꾸준한 참여 유도
- **성취감**: 업적 시스템을 통한 자기 발전 추적

### 1.2 주요 기능 요구사항

#### 1.2.1 사용자 관리
- **회원가입**: 이메일, 비밀번호, 닉네임으로 계정 생성
- **로그인**: JWT 토큰 기반 인증
- **내 정보 조회**: 사용자 기본 정보 확인
- **정보 수정**: 비밀번호/닉네임 변경
- **게임 통계 조회**: 총 플레이 횟수, 승률, 평균 시도 횟수 등

#### 1.2.2 게임 플레이
- **단어 추측 제출**: 단어 입력 → 유사도 및 순위 확인
- **게임 포기**: 정답 공개 및 상위 유사 단어 확인
- **게임 상태 조회**: NOT_STARTED, IN_PROGRESS, SOLVED, GAVE_UP
- **정답 이력 조회**: 오늘/어제 정답 및 상위 100개 단어

#### 1.2.3 리더보드
- **순위 조회**: 상위 50명 + 본인 순위
- **순위 결정 기준**: 시도 횟수 적을수록, 같으면 빨리 푼 사람 우선

#### 1.2.4 업적 시스템
- **연속 풀이 업적**: 3일/7일/30일 연속
- **총 문제 업적**: 10개/50개/100개 해결
- **자동 부여**: 정답 맞출 때마다 조건 체크

#### 1.2.5 일일 배치 작업
- **유지보수 모드**: 매일 자정 배치 작업 중 API 차단
- **최고 순위 갱신**: 전날 Top 50 사용자의 bestRank 업데이트
- **연속 풀이 초기화**: 오늘 풀지 않은 사용자의 nowCont = 0

### 1.3 비기능 요구사항

#### 1.3.1 성능
- API 응답 시간: 평균 200ms 이하
- 동시 접속 사용자: 1,000명 이상
- Redis 캐시 히트율: 90% 이상

#### 1.3.2 보안
- JWT 기반 인증 (Access Token: 1시간, Refresh Token: 7일)
- 비밀번호 BCrypt 암호화
- HTTPS 통신 강제

#### 1.3.3 가용성
- 시스템 가동률(Uptime): 99.5% 이상
- 데이터베이스 일일 백업

### 1.4 기술 스택

| 분류 | 기술 |
|------|------|
| **언어** | Java 17 |
| **프레임워크** | Spring Boot 3.x |
| **빌드 도구** | Gradle (멀티모듈) |
| **데이터베이스** | MySQL 8.0 |
| **캐시** | Redis 6.0+ |
| **ORM** | MyBatis 3.x |
| **보안** | Spring Security, JWT |
| **아키텍처** | 헥사고날 아키텍처, DDD |

---

## 2. Use Case 다이어그램

### 2.1 전체 시스템 Use Case

싸멘틀 시스템의 전체 Use Case는 다음과 같습니다:

![UseCase 다이어그램](./UseCase.png)

### 2.2 주요 액터

| 액터 | 설명 |
|------|------|
| **일반 사용자** | 게임 플레이, 회원 관리, 리더보드 조회 등 |
| **관리자** | 일일 배치 작업 실행, 시스템 관리 |
| **스케줄러** | 매일 자정 자동 배치 작업 트리거 |

### 2.3 Use Case 목록

#### 인증 및 사용자 관리
- UC-001: 회원가입
- UC-002: 로그인
- UC-003: 토큰 갱신
- UC-004: 내 정보 조회
- UC-005: 사용자 정보 수정
- UC-006: 게임 통계 조회

#### 게임 플레이
- UC-007: 단어 추측 제출
- UC-008: 게임 포기
- UC-009: 게임 상태 조회
- UC-010: 오늘 정답 이력 조회
- UC-011: 어제 정답 이력 조회

#### 리더보드 및 업적
- UC-012: 리더보드 조회
- UC-013: 내 업적 조회

#### 시스템 관리
- UC-014: 일일 배치 실행

### 2.4 Use Case 다이어그램 (상세)

#### 2.4.1 전체 시스템 Use Case

![전체 시스템 Use Case](./1.png)

#### 2.4.2 게임 플레이 Use Case (상세)

![게임 플레이 Use Case](./2.png)

### 2.5 주요 시퀀스 다이어그램

#### 2.5.1 로그인 시퀀스

![로그인 시퀀스](./3.png)

#### 2.5.2 단어 추측 제출 시퀀스 (정답인 경우)

![단어 추측 제출 시퀀스 (정답)](./4.png)

#### 2.5.3 단어 추측 제출 시퀀스 (오답인 경우)

![단어 추측 제출 시퀀스 (오답)](./5.png)

#### 2.5.4 리더보드 조회 시퀀스

![리더보드 조회 시퀀스](./6.png)

#### 2.5.5 일일 배치 작업 시퀀스

![일일 배치 작업 시퀀스](./7.png)

---

## 3. 클래스 다이어그램

### 3.1 헥사고날 아키텍처 - 전체 레이어 통합뷰

싸멘틀은 헥사고날 아키텍처(Ports & Adapters 패턴)를 기반으로 설계되었습니다.


### 3.2 클래스 다이어그램 이미지

#### 3.2.1 도메인 및 애플리케이션 계층
![클래스 다이어그램 1](./class1_Back.png)

### 3.3 주요 계층 및 패턴

| 계층 | 주요 클래스 패턴 | 책임 |
|------|-----------------|------|
| **Inbound Adapter** | XXXController | HTTP 요청/응답 처리, 인증 확인, DTO 변환 |
| **Inbound Port** | XXXUseCase (interface) | 애플리케이션 진입점 정의 |
| **Application Service** | XXXService | Use Case 구현, 도메인 로직 오케스트레이션 |
| **Domain** | Entity, ValueObject | 비즈니스 규칙, 엔티티 행위, 값 객체 검증 |
| **Outbound Port** | LoadXXXPort, SaveXXXPort (interface) | 외부 시스템 추상화 |
| **Outbound Adapter (RDB)** | XXXPersistenceAdapter | 데이터베이스 영속성, 도메인 ↔ 엔티티 변환 |
| **Outbound Adapter (Redis)** | XXXRedisAdapter | 캐시 조회/저장, Top 1000, 리더보드 관리 |
| **Outbound Adapter (External)** | XXXExternalAdapter | 외부 API 호출 (유사도 계산) |

### 3.4 의존성 규칙

```
bootstrap → adapter → application → domain
```

- **domain**: 의존성 없음 (순수 Java)
- **application**: domain만 의존
- **adapter**: application, domain 의존
- **bootstrap**: 모든 모듈 의존

---

## 4. ERD (Entity Relationship Diagram)

### 4.1 데이터베이스 설계

싸멘틀의 데이터베이스 ERD는 다음과 같습니다:

![ERD](./ERD.png)

### 4.2 주요 테이블

#### 4.2.1 users (사용자)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 사용자 ID |
| email | VARCHAR(255) | UNIQUE, NOT NULL | 이메일 |
| password | VARCHAR(255) | NOT NULL | 암호화된 비밀번호 |
| nickname | VARCHAR(20) | UNIQUE, NOT NULL | 닉네임 |
| role | VARCHAR(20) | NOT NULL, DEFAULT 'CUSTOMER' | 역할 |
| today_solve | BOOLEAN | NOT NULL, DEFAULT false | 오늘 풀이 여부 |
| longest_cont | INT | NOT NULL, DEFAULT 0 | 최장 연속 풀이 일수 |
| now_cont | INT | NOT NULL, DEFAULT 0 | 현재 연속 풀이 일수 |
| best_rank | INT | NULL | 최고 순위 |
| is_delete | BOOLEAN | NOT NULL, DEFAULT false | 삭제 여부 |
| created_at | DATETIME | NOT NULL | 생성 시각 |
| updated_at | DATETIME | NOT NULL | 수정 시각 |

#### 4.2.2 problems (문제)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 문제 ID |
| answer | VARCHAR(100) | NOT NULL | 정답 단어 |
| date | DATE | UNIQUE, NOT NULL | 문제 날짜 |

#### 4.2.3 records (기록)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 기록 ID |
| user_id | BIGINT | FK(users.id), NOT NULL | 사용자 ID |
| problem_id | BIGINT | FK(problems.id), NOT NULL | 문제 ID |
| fail_count | INT | NOT NULL, DEFAULT 0 | 실패 횟수 |
| solved_at | DATETIME | NULL | 해결 시각 |
| give_up_at | DATETIME | NULL | 포기 시각 |
| created_at | DATETIME | NOT NULL | 생성 시각 |

#### 4.2.4 user_achievements (사용자 업적)
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|---------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 업적 ID |
| user_id | BIGINT | FK(users.id), NOT NULL | 사용자 ID |
| achievement_type | VARCHAR(50) | NOT NULL | 업적 타입 |
| unlocked_at | DATETIME | NOT NULL | 획득 시각 |

### 4.3 주요 관계
- User (1) - (N) Record: 한 사용자는 여러 기록을 가질 수 있음
- Problem (1) - (N) Record: 한 문제는 여러 기록을 가질 수 있음
- User (1) - (N) UserAchievement: 한 사용자는 여러 업적을 획득할 수 있음

### 4.4 인덱스 전략
- users: email, nickname (UNIQUE)
- problems: date (UNIQUE)
- records: (user_id, problem_id) (UNIQUE), solved_at
- user_achievements: user_id, (user_id, achievement_type) (UNIQUE)

---

## 5. WBS & 간트 차트

### 5.1 Work Breakdown Structure (WBS)

#### 1. 프로젝트 계획 (1주)
- 1.1 요구사항 분석
- 1.2 기술 스택 선정
- 1.3 아키텍처 설계
- 1.4 데이터베이스 설계

#### 2. 개발 환경 구축 (1주)
- 2.1 멀티모듈 프로젝트 구조 설정
- 2.2 Spring Boot 초기 설정
- 2.3 데이터베이스 연결 설정
- 2.4 Redis 연동 설정
- 2.5 CI/CD 파이프라인 구축

#### 3. 도메인 계층 개발 (1주)
- 3.1 User 도메인 모델
  - 3.1.1 User 엔티티
  - 3.1.2 Email, Password, Nickname 값 객체
  - 3.1.3 도메인 예외 정의
- 3.2 Problem 도메인 모델
- 3.3 Record 도메인 모델
- 3.4 Achievement 도메인 모델
- 3.5 게임 관련 값 객체 (WordSimilarity, LeaderboardScore)

#### 4. 애플리케이션 계층 개발 (2주)
- 4.1 사용자 관리
  - 4.1.1 회원가입 UseCase & Service
  - 4.1.2 로그인 UseCase & Service
  - 4.1.3 정보 수정 UseCase & Service
  - 4.1.4 통계 조회 UseCase & Service
- 4.2 게임 플레이
  - 4.2.1 단어 추측 UseCase & Service
  - 4.2.2 게임 포기 UseCase & Service
  - 4.2.3 게임 상태 조회 UseCase & Service
  - 4.2.4 정답 이력 조회 UseCase & Service
- 4.3 리더보드 UseCase & Service
- 4.4 업적 시스템 UseCase & Service
- 4.5 일일 배치 UseCase & Service

#### 5. 어댑터 계층 개발 (2주)
- 5.1 Inbound Adapter (HTTP)
  - 5.1.1 Controller 구현
  - 5.1.2 DTO 정의
  - 5.1.3 JWT 인증 필터
  - 5.1.4 전역 예외 처리
- 5.2 Outbound Adapter (RDB)
  - 5.2.1 MyBatis Mapper 작성
  - 5.2.2 Persistence Adapter 구현
  - 5.2.3 엔티티 매핑
- 5.3 Outbound Adapter (Redis)
  - 5.3.1 Game Redis Adapter
  - 5.3.2 Leaderboard Redis Adapter
  - 5.3.3 Maintenance Redis Adapter
- 5.4 Outbound Adapter (External API)
  - 5.4.1 추론 서버 연동 Adapter

#### 6. 파이썬 추론 서버 개발 (1주)
- 6.1 Word2Vec 모델 학습
- 6.2 유사도 계산 API 구현
- 6.3 Top 1000 단어 사전 계산
- 6.4 Redis 데이터 저장 로직

#### 7. 테스트 (1주)
- 7.1 단위 테스트 작성
- 7.2 통합 테스트 작성
- 7.3 E2E 테스트
- 7.4 부하 테스트

#### 8. 배포 및 운영 (1주)
- 8.1 프로덕션 환경 구축
- 8.2 모니터링 설정
- 8.3 로그 수집 시스템 구축
- 8.4 백업 전략 수립

#### 9. 문서화 (진행 중)
- 9.1 API 문서 작성
- 9.2 아키텍처 문서 작성
- 9.3 운영 가이드 작성

### 5.2 간트 차트
![간트차트](./8.png)


### 5.3 주요 마일스톤

| 마일스톤 | 날짜 | 산출물 |
|---------|------|--------|
| **M1: 설계 완료** | 2025-11-27 | 요구사항 명세서, 아키텍처 설계서, ERD |
| **M2: 환경 구축 완료** | 2025-12-01 | 멀티모듈 설정, Spring Boot 설정, DB/Redis 연동 |
| **M3: 도메인 개발 완료** | 2025-12-05 | 도메인 모델, 도메인 서비스 |
| **M4: 애플리케이션 개발 완료** | 2025-12-12 | UseCase, Service, Port 인터페이스 |
| **M5: 어댑터 개발 완료** | 2025-12-18 | Controller, Persistence Adapter, Redis Adapter |
| **M6: 추론 서버 완료** | 2025-12-21 | Word2Vec 모델, 유사도 API |
| **M7: 테스트 및 배포 완료** | 2025-12-24 | 테스트 리포트, 프로덕션 환경 |

---

## 6. 모듈 구조

### 6.1 멀티모듈 프로젝트 구조

싸멘틀은 헥사고날 아키텍처를 기반으로 한 멀티모듈 프로젝트입니다.

![모듈 구조](./module.png)

### 6.2 모듈별 설명

#### 6.2.1 domain 모듈
- **패키지**: `com.pigeon3.ssamantle.domain.model`
- **역할**: 핵심 비즈니스 로직, 도메인 모델
- **의존성**: 없음 (순수 Java)
- **주요 구성**:
  - Entity: User, Problem, Record, UserAchievement
  - Value Object: Email, Password, Nickname, WordSimilarity
  - Domain Service: 도메인 로직
  - Domain Exception: 도메인 예외

#### 6.2.2 application 모듈
- **패키지**: `com.pigeon3.ssamantle.application`
- **역할**: Use Case 정의 및 구현
- **의존성**: domain
- **주요 구성**:
  - Inbound Port: XXXUseCase (interface)
  - Service: XXXService (UseCase 구현)
  - Outbound Port: LoadXXXPort, SaveXXXPort (interface)
  - Command/Response: DTO

#### 6.2.3 adapter-in-http 모듈
- **패키지**: `com.pigeon3.ssamantle.adapter.in.http`
- **역할**: REST API 제공
- **의존성**: application, domain
- **주요 구성**:
  - Controller: HTTP 요청/응답 처리
  - DTO: Request/Response
  - Security: JWT 인증
  - Exception Handler: 전역 예외 처리

#### 6.2.4 adapter-out-rdb 모듈
- **패키지**: `com.pigeon3.ssamantle.adapter.out.rdb`
- **역할**: MySQL 영속성
- **의존성**: application, domain
- **주요 구성**:
  - Persistence Adapter: Port 구현
  - MyBatis Mapper: SQL 매핑
  - Entity: RDB 엔티티
  - Mapper: 도메인 ↔ 엔티티 변환

#### 6.2.5 adapter-out-in-memory 모듈
- **패키지**: `com.pigeon3.ssamantle.adapter.out.inmemory`
- **역할**: Redis 캐싱
- **의존성**: application, domain
- **주요 구성**:
  - Redis Adapter: 캐시 조회/저장
  - Leaderboard Adapter: 리더보드 관리
  - Maintenance Adapter: 유지보수 모드

#### 6.2.6 adapter-out-inference 모듈
- **패키지**: `com.pigeon3.ssamantle.adapter.out.inference`
- **역할**: 추론 서버 연동
- **의존성**: application, domain
- **주요 구성**:
  - Inference Adapter: 유사도 계산 API 호출
  - WebClient: HTTP 통신

#### 6.2.7 bootstrap 모듈
- **패키지**: `com.pigeon3.ssamantle.bootstrap`
- **역할**: 애플리케이션 시작점
- **의존성**: 모든 모듈
- **주요 구성**:
  - BootstrapApplication: Spring Boot Main
  - Configuration: 빈 설정

### 6.3 의존성 그래프

```
bootstrap
    ├── adapter-in-http
    │   ├── application
    │   │   └── domain
    │   └── domain
    ├── adapter-out-rdb
    │   ├── application
    │   │   └── domain
    │   └── domain
    ├── adapter-out-in-memory
    │   ├── application
    │   │   └── domain
    │   └── domain
    └── adapter-out-inference
        ├── application
        │   └── domain
        └── domain
```

### 6.4 빌드 설정 (Gradle)

```gradle
// settings.gradle
rootProject.name = 'ssamatle-server'

include 'domain'
include 'application'
include 'adapter:in:http'
include 'adapter:out:rdb'
include 'adapter:out:in-memory'
include 'adapter:out:inference'
include 'bootstrap'
```

---

## 7. 화면 설계서
### 1. 초기 화면(Welcome)

#### 1.1. 로그인 화면

![image.png](image.png)

#### 1.2. 회원가입 화면

##### 1.2.1. 회원가입 성공

![image.png](image%201.png)

---

### 2. 메인 게임 화면(main)

#### 2.1. Intro

- 로그인 후  첫 진입 시 나타나는 화면

![image.png](image%202.png)

#### 2.2. 화면 구성

##### 2.2.1. Header

![image.png](image%203.png)

##### 2.2.2. Footer

![image.png](image%204.png)

##### 2.2.3 메인 화면

![image.png](image%205.png)

#### 2.3. 메인 게임 플로우

##### 2.3.1. 오답 시

![image.png](image%206.png)

##### 2.3.2. 정답 시

![image.png](image%207.png)

##### 2.3.3. 게임 포기 시

- 추측 단어가 1개 이상이어야 포기 가능

![image.png](image%208.png)

![image.png](image%209.png)

![image.png](image%2010.png)

---

# 3. 메인 페이지 Header Navigation

## 3.1. About 페이지

![image.png](image%2011.png)

## 3.2. Leaderboard 페이지

![image.png](image%2012.png)

## 3.3. History 페이지

![image.png](image%2013.png)

## 3.4. FAQ 페이지

![image.png](image%2014.png)

## 3.5. 마이페이지

![image.png](image%2015.png)

### 3.5.1. 마이페이지 > 정보 수정 페이지

![image.png](image%2016.png)

![image.png](image%2017.png)

![image.png](image%2018.png)

![image.png](image%2019.png)

![image.png](image%2020.png)

## 3.6. 로그아웃

![image.png](image%2021.png)
---

**END OF DOCUMENT**
