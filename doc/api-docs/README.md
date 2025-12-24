# 싸멘틀 API 문서

이 문서는 싸멘틀 서비스의 전체 API를 설명합니다.

## 목차

### 인증 API
1. [로그인](#1-로그인)
2. [토큰 갱신](#2-토큰-갱신)

### 사용자 API
3. [회원가입](#3-회원가입)
4. [내 정보 조회](#4-내-정보-조회)
5. [사용자 정보 수정](#5-사용자-정보-수정)
6. [내 게임 통계 조회](#6-내-게임-통계-조회)

### 게임 API
7. [단어 추측 제출](#7-단어-추측-제출)
8. [게임 포기](#8-게임-포기)
9. [게임 상태 조회](#9-게임-상태-조회)
10. [오늘 정답 이력 조회](#10-오늘-정답-이력-조회)
11. [어제 정답 이력 조회](#11-어제-정답-이력-조회)

### 리더보드 API
12. [리더보드 조회](#12-리더보드-조회)

### 업적 API
13. [내 업적 목록 조회](#13-내-업적-목록-조회)

---

# 인증 API

## 1. 로그인

### Endpoint
```
POST /api/v1/auth/sign-in
```

### Headers
```
Content-Type: application/json
```

### Request Body
```json
{
  "email": "user@example.com",
  "password": "Password123!@"
}
```

파일: [sign-in-request.json](auth/sign-in-request.json)

### Response - 성공 (200 OK)
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "email": "user@example.com",
    "nickname": "user123",
    "role": "CUSTOMER",
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "error": null
}
```

파일: [sign-in-response.json](auth/sign-in-response.json)

### Response - 인증 실패 (401 Unauthorized)
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A001",
    "message": "이메일 또는 비밀번호가 일치하지 않습니다."
  }
}
```

파일: [sign-in-response-invalid-credentials.json](auth/sign-in-response-invalid-credentials.json)

---

## 2. 토큰 갱신

### Endpoint
```
POST /api/v1/auth/refresh
```

### Headers
```
Content-Type: application/json
```

### Request Body
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

파일: [refresh-request.json](auth/refresh-request.json)

### Response - 성공 (200 OK)
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "error": null
}
```

파일: [refresh-response.json](auth/refresh-response.json)

---

# 사용자 API

## 3. 회원가입

### Endpoint
```
POST /api/v1/users
```

### Headers
```
Content-Type: application/json
```

### Request Body
```json
{
  "email": "newuser@example.com",
  "password": "Password1!",
  "nickname": "새로운사용자"
}
```

파일: [sign-up-request.json](users/sign-up-request.json)

### Response - 성공 (201 Created)
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "email": "newuser@example.com",
    "nickname": "새로운사용자",
    "role": "CUSTOMER"
  },
  "error": null
}
```

파일: [sign-up-response.json](users/sign-up-response.json)

### Response - 이메일 중복 (409 Conflict)
```json
{
  "success": false,
  "data": null,
  "error": {
    "errorCode": "USER_002",
    "message": "이미 존재하는 이메일입니다."
  }
}
```

파일: [sign-up-response-duplicate-email.json](users/sign-up-response-duplicate-email.json)

### Response - 비밀번호 유효성 검증 실패 (400 Bad Request)
```json
{
  "success": false,
  "data": null,
  "error": {
    "errorCode": "VALIDATION_ERROR",
    "message": "비밀번호는 알파벳 대소문자, 숫자, 특수문자(@$!%*?&#)를 모두 포함해야 합니다."
  }
}
```

파일: [sign-up-response-invalid-password.json](users/sign-up-response-invalid-password.json)

---

## 4. 내 정보 조회

### Endpoint
```
GET /api/v1/users/me
```

### Headers
```
Authorization: Bearer {accessToken}
```

### Response - 성공 (200 OK)
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "email": "user@example.com",
    "nickname": "user123",
    "role": "CUSTOMER"
  },
  "error": null
}
```

파일: [get-my-info-response.json](users/get-my-info-response.json)

### Response - 사용자 없음 (404 Not Found)
```json
{
  "success": false,
  "data": null,
  "error": {
    "errorCode": "USER_100",
    "message": "사용자를 찾을 수 없습니다."
  }
}
```

파일: [get-my-info-response-not-found.json](users/get-my-info-response-not-found.json)

---

## 5. 사용자 정보 수정

### Endpoint
```
PATCH /api/v1/users/me
```

### Headers
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### Request Body
비밀번호와 닉네임 중 하나 이상 포함 (둘 다 선택적)

```json
{
  "password": "NewPassword1!",
  "nickname": "수정된닉네임"
}
```

파일: [update-user-request.json](users/update-user-request.json)

닉네임만 수정: [update-user-request-nickname-only.json](users/update-user-request-nickname-only.json)

비밀번호만 수정: [update-user-request-password-only.json](users/update-user-request-password-only.json)

### Response - 성공 (200 OK)
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "email": "user@example.com",
    "nickname": "수정된닉네임",
    "message": "유저 정보가 성공적으로 수정되었습니다."
  },
  "error": null
}
```

파일: [update-user-response.json](users/update-user-response.json)

---

## 6. 내 게임 통계 조회

### Endpoint
```
GET /api/v1/users/me/statistics
```

### Headers
```
Authorization: Bearer {accessToken}
```

### Request Body
요청 본문 없음

### Response - 성공 (200 OK)
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "totalGamesPlayed": 50,
    "successfulGames": 35,
    "winRate": 70.0,
    "bestRank": 3,
    "longestConsecutiveDays": 15,
    "averageAttempts": 4.2
  },
  "error": null
}
```

파일: [get-my-statistics-response.json](users/get-my-statistics-response.json)

### 필드 설명
| 필드 | 타입 | 설명 |
|------|------|------|
| userId | integer | 사용자 ID |
| totalGamesPlayed | integer | 총 게임 플레이 횟수 |
| successfulGames | integer | 성공한 게임 횟수 |
| winRate | double | 승률 (0.0 ~ 100.0) |
| bestRank | integer | 최고 등수 (null 가능) |
| longestConsecutiveDays | integer | 최장 연속 풀이 일수 |
| averageAttempts | double | 평균 시도 횟수 |

---

# 게임 API

## 7. 단어 추측 제출

### Endpoint
```
POST /api/v1/games/guess
```

### Headers
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### Request Body
```json
{
  "word": "사과",        // 추측한 단어 (필수, NotBlank)
  "failCount": 3         // 현재 시도 횟수 (필수, 0 이상)
}
```

파일: [guess-request.json](games/guess-request.json)

### Response - 정답인 경우 (200 OK)
```json
{
  "success": true,
  "data": {
    "isCorrect": true,
    "message": "정답입니다!",
    "word": "사과",
    "similarity": null,
    "rank": null,
    "answer": "사과",
    "failCount": 3,
    "newAchievements": null
  },
  "error": null
}
```

파일: [guess-response-correct.json](games/guess-response-correct.json)

### Response - 정답이고 새 업적 획득한 경우 (200 OK)
```json
{
  "success": true,
  "data": {
    "isCorrect": true,
    "message": "정답입니다!",
    "word": "사과",
    "similarity": null,
    "rank": null,
    "answer": "사과",
    "failCount": 3,
    "newAchievements": [
      {
        "type": "STREAK_3_DAYS",
        "title": "3일 연속 풀이",
        "description": "3일 연속으로 문제를 해결했습니다"
      },
      {
        "type": "TOTAL_10_SOLVED",
        "title": "문제 10개 해결",
        "description": "총 10개의 문제를 해결했습니다"
      }
    ]
  },
  "error": null
}
```

파일: [guess-response-correct-with-achievements.json](games/guess-response-correct-with-achievements.json)

### Response - 오답인 경우 (200 OK)
```json
{
  "success": true,
  "data": {
    "isCorrect": false,
    "message": "오답입니다.",
    "word": "바나나",
    "similarity": 85.42,    // 유사도 (0.0 ~ 100.0)
    "rank": 123,            // 순위 (1 ~ 10000)
    "answer": null,
    "failCount": 0
  },
  "error": null
}
```

파일: [guess-response-wrong.json](games/guess-response-wrong.json)

### Response - 이미 해결된 문제 (409 Conflict)
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "G003",
    "message": "이미 해결된 문제입니다."
  }
}
```

파일: [guess-response-already-solved.json](games/guess-response-already-solved.json)

### Response - 이미 포기한 문제 (409 Conflict)
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "G004",
    "message": "이미 포기한 문제입니다."
  }
}
```

파일: [guess-response-already-gave-up.json](games/guess-response-already-gave-up.json)

### Response - 없는 단어 (400 Bad Request)
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "G007",
    "message": "없는 단어입니다."
  }
}
```

파일: [guess-response-word-not-found.json](games/guess-response-word-not-found.json)

---

## 8. 게임 포기

### Endpoint
```
POST /api/v1/games/give-up
```

### Headers
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### Request Body
요청 본문 없음

### Response - 성공 (200 OK)
```json
{
  "success": true,
  "data": {
    "message": "포기하셨습니다. 정답은 '사과' 입니다.",
    "answer": "사과",
    "failCount": 5,
    "giveUpAt": "2025-12-15T14:30:25.123456"
  },
  "error": null
}
```

파일: [give-up-response.json](games/give-up-response.json)

### Response - 진행 중인 게임이 없음 (404 Not Found)
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "G005",
    "message": "진행 중인 게임이 없습니다."
  }
}
```

파일: [give-up-response-no-game.json](games/give-up-response-no-game.json)

---

## 9. 게임 상태 조회

### Endpoint
```
GET /api/v1/games/status
```

### Headers
```
Authorization: Bearer {accessToken}
```

### Request Body
요청 본문 없음

### 설명
사용자의 오늘 게임 진행 상태를 조회합니다.
상태는 다음 4가지 중 하나입니다:
- `NOT_STARTED`: 오늘 문제를 시작하지 않음
- `IN_PROGRESS`: 문제를 풀고 있는 중
- `SOLVED`: 문제를 해결함
- `GAVE_UP`: 문제를 포기함

### Response - 시작하지 않음 (200 OK)
```json
{
  "success": true,
  "data": {
    "status": "NOT_STARTED",
    "failCount": 0
  },
  "error": null
}
```

파일: [get-game-status-response-not-started.json](games/get-game-status-response-not-started.json)

### Response - 진행 중 (200 OK)
```json
{
  "success": true,
  "data": {
    "status": "IN_PROGRESS",
    "failCount": 5
  },
  "error": null
}
```

파일: [get-game-status-response-in-progress.json](games/get-game-status-response-in-progress.json)

### Response - 문제 해결함 (200 OK)
```json
{
  "success": true,
  "data": {
    "status": "SOLVED",
    "failCount": 8
  },
  "error": null
}
```

파일: [get-game-status-response-solved.json](games/get-game-status-response-solved.json)

### Response - 포기함 (200 OK)
```json
{
  "success": true,
  "data": {
    "status": "GAVE_UP",
    "failCount": 12
  },
  "error": null
}
```

파일: [get-game-status-response-gave-up.json](games/get-game-status-response-gave-up.json)

### 필드 설명
| 필드 | 타입 | 설명 |
|------|------|------|
| status | string | 게임 상태 (NOT_STARTED, IN_PROGRESS, SOLVED, GAVE_UP) |
| failCount | integer | 현재까지 실패한 횟수 (NOT_STARTED인 경우 0) |

---

## 10. 오늘 정답 이력 조회

### Endpoint
```
GET /api/v1/games/answer-history/today
```

### Headers
```
Authorization: Bearer {accessToken}
```

### Request Body
요청 본문 없음

### 설명
오늘 문제의 정답 단어와 유사도 상위 100개 단어를 조회합니다.
**문제를 풀었거나 포기해야만 조회 가능**합니다. 진행 중인 경우 answer와 top100Words는 null을 반환합니다.

### Response - 성공 (권한 있음, 200 OK)
```json
{
  "success": true,
  "data": {
    "date": "2025-12-22",
    "answer": "사과",
    "top100Words": [
      {
        "word": "과일",
        "similarity": 95.5,
        "rank": 1
      },
      {
        "word": "배",
        "similarity": 92.3,
        "rank": 2
      },
      {
        "word": "바나나",
        "similarity": 89.1,
        "rank": 3
      }
    ]
  },
  "error": null
}
```

파일: [answer-history-today-response.json](games/answer-history-today-response.json)

### Response - 성공 (권한 없음, 200 OK)
문제를 풀지 않았거나 진행 중인 경우
```json
{
  "success": true,
  "data": {
    "date": "2025-12-22",
    "answer": null,
    "top100Words": null
  },
  "error": null
}
```

파일: [answer-history-today-response-no-permission.json](games/answer-history-today-response-no-permission.json)

### Response - 문제가 없는 경우 (200 OK)
```json
{
  "success": true,
  "data": {
    "date": "2025-12-22",
    "answer": null,
    "top100Words": null
  },
  "error": null
}
```

파일: [answer-history-response-no-problem.json](games/answer-history-response-no-problem.json)

---

## 11. 어제 정답 이력 조회

### Endpoint
```
GET /api/v1/games/answer-history/yesterday
```

### Headers
```
Authorization: Bearer {accessToken}
```

### Request Body
요청 본문 없음

### 설명
어제 문제의 정답 단어와 유사도 상위 100개 단어를 조회합니다.
**권한 확인 없이 누구나 조회 가능**합니다.

### Response - 성공 (200 OK)
```json
{
  "success": true,
  "data": {
    "date": "2025-12-21",
    "answer": "딸기",
    "top100Words": [
      {
        "word": "과일",
        "similarity": 94.2,
        "rank": 1
      },
      {
        "word": "사과",
        "similarity": 91.8,
        "rank": 2
      },
      {
        "word": "체리",
        "similarity": 88.5,
        "rank": 3
      }
    ]
  },
  "error": null
}
```

파일: [answer-history-yesterday-response.json](games/answer-history-yesterday-response.json)

### 필드 설명
| 필드 | 타입 | 설명 |
|------|------|------|
| date | string | 문제 날짜 (YYYY-MM-DD) |
| answer | string \| null | 정답 단어 (권한이 없거나 문제가 없으면 null) |
| top100Words | array \| null | 유사도 상위 100개 단어 (권한이 없거나 문제가 없으면 null) |
| word | string | 단어 |
| similarity | double | 유사도 (0.0 ~ 100.0) |
| rank | integer | 순위 (1부터 시작) |

---

# 리더보드 API

## 12. 리더보드 조회

### Endpoint
```
GET /api/v1/leaderboard?date=2025-12-22
```

### Headers
```
Authorization: Bearer {accessToken}
```

### Query Parameters
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| date | string (YYYY-MM-DD) | 아니오 | 조회할 날짜 (생략 시 오늘 날짜) |

### Request Body
요청 본문 없음

### Response - 성공 (200 OK)
```json
{
  "success": true,
  "data": {
    "date": "2025-12-22",
    "topRankers": [
      {
        "rank": 1,
        "nickname": "유저1",
        "failCount": 2,
        "solvedAt": "2025-12-22T08:15:30"
      },
      {
        "rank": 2,
        "nickname": "유저2",
        "failCount": 3,
        "solvedAt": "2025-12-22T09:20:15"
      },
      {
        "rank": 3,
        "nickname": "유저3",
        "failCount": 3,
        "solvedAt": "2025-12-22T10:05:42"
      }
    ],
    "myRank": {
      "rank": 15,
      "nickname": "내닉네임",
      "failCount": 5,
      "solvedAt": "2025-12-22T14:30:25"
    }
  },
  "error": null
}
```

파일: [get-leaderboard-response.json](leaderboard/get-leaderboard-response.json)

### Response - 내 순위가 없는 경우 (200 OK)
문제를 풀지 않았거나 포기한 경우 `myRank`가 `null`입니다.
```json
{
  "success": true,
  "data": {
    "date": "2025-12-22",
    "topRankers": [
      {
        "rank": 1,
        "nickname": "유저1",
        "failCount": 2,
        "solvedAt": "2025-12-22T08:15:30"
      }
    ],
    "myRank": null
  },
  "error": null
}
```

파일: [get-leaderboard-response-no-my-rank.json](leaderboard/get-leaderboard-response-no-my-rank.json)

### 필드 설명
| 필드 | 타입 | 설명 |
|------|------|------|
| date | string | 리더보드 날짜 (YYYY-MM-DD) |
| topRankers | array | 상위 랭커 목록 (최대 100명) |
| myRank | object \| null | 내 순위 정보 (문제를 풀지 않았으면 null) |
| rank | integer | 순위 (1부터 시작) |
| nickname | string | 닉네임 |
| failCount | integer | 실패 횟수 |
| solvedAt | datetime | 문제 해결 시각 (ISO 8601) |

### 순위 결정 규칙
1. `failCount` 오름차순 (시도 횟수가 적을수록 높은 순위)
2. `failCount`가 같으면 `solvedAt` 오름차순 (먼저 푼 사람이 높은 순위)

---

## 공통 에러 응답

### 인증 실패 (401 Unauthorized)
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "A002",
    "message": "유효하지 않은 토큰입니다."
  }
}
```

### 오늘의 문제 없음 (404 Not Found)
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "G001",
    "message": "오늘의 문제를 찾을 수 없습니다."
  }
}
```

### 유효성 검증 실패 (400 Bad Request)
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "C422",
    "message": "추측 단어는 필수입니다."
  }
}
```

---

## 게임 플로우

### 1. 첫 번째 시도 (새 게임 시작)
```
POST /api/v1/games/guess
Request: { "word": "바나나", "failCount": 1 }
→ 기록(Record)이 없으면 자동 생성
→ Response: { "isCorrect": false, "similarity": 45.2, "rank": 234 }
```

### 2. 두 번째 시도
```
POST /api/v1/games/guess
Request: { "word": "딸기", "failCount": 2 }
→ 기존 기록 조회
→ Response: { "isCorrect": false, "similarity": 62.1, "rank": 156 }
```

### 3. 정답 맞춤
```
POST /api/v1/games/guess
Request: { "word": "사과", "failCount": 3 }
→ Record.failCount = 3, Record.solvedAt = 현재시간
→ User.solveProblem() 호출 (todaySolve = true, 연속기록 증가)
→ Response: { "isCorrect": true, "answer": "사과", "failCount": 3 }
```

### 4. 이미 맞춘 문제에 재시도
```
POST /api/v1/games/guess
Request: { "word": "무엇이든", "failCount": 4 }
→ Error: { "code": "G003", "message": "이미 해결된 문제입니다." }
```

### 5. 포기하기
```
POST /api/v1/games/give-up
→ Record.giveUpAt = 현재시간
→ Response: { "answer": "사과", "giveUpAt": "..." }
```

---

## 필드 설명

### SubmitGuessResponse
| 필드 | 타입 | 설명 | 조건 |
|------|------|------|------|
| isCorrect | boolean | 정답 여부 | 항상 포함 |
| message | string | 응답 메시지 | 항상 포함 |
| word | string | 제출한 단어 | 항상 포함 |
| similarity | double | 유사도 (0.0~100.0) | 오답일 때만 |
| rank | integer | 순위 (1~10000) | 오답일 때만 |
| answer | string | 정답 단어 | 정답일 때만 |
| failCount | integer | 시도 횟수 | 항상 포함 |
| newAchievements | array \| null | 새로 획득한 업적 목록 | 정답일 때만 (없으면 null) |

### GiveUpGameResponse
| 필드 | 타입 | 설명 |
|------|------|------|
| message | string | 포기 메시지 |
| answer | string | 정답 단어 |
| failCount | integer | 최종 시도 횟수 |
| giveUpAt | datetime | 포기 시간 (ISO 8601) |

---

# 업적 API

## 13. 내 업적 목록 조회

### Endpoint
```
GET /api/v1/achievements/me
```

### Headers
```
Authorization: Bearer {accessToken}
```

### Request Body
요청 본문 없음

### 설명
현재 로그인한 사용자가 획득한 모든 업적을 조회합니다.
업적은 획득한 시간 순서대로 최신순으로 정렬됩니다.

### Response - 성공 (200 OK)
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "achievements": [
      {
        "type": "STREAK_3_DAYS",
        "title": "3일 연속 풀이",
        "description": "3일 연속으로 문제를 해결했습니다",
        "unlockedAt": "2025-12-20T14:30:25"
      },
      {
        "type": "TOTAL_10_SOLVED",
        "title": "문제 10개 해결",
        "description": "총 10개의 문제를 해결했습니다",
        "unlockedAt": "2025-12-22T09:15:10"
      }
    ]
  },
  "error": null
}
```

파일: [get-my-achievements-response.json](achievements/get-my-achievements-response.json)

### Response - 업적이 없는 경우 (200 OK)
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "achievements": []
  },
  "error": null
}
```

파일: [get-my-achievements-response-empty.json](achievements/get-my-achievements-response-empty.json)

### 필드 설명
| 필드 | 타입 | 설명 |
|------|------|------|
| userId | integer | 사용자 ID |
| achievements | array | 획득한 업적 목록 (최신순 정렬) |
| type | string | 업적 타입 (enum 값) |
| title | string | 업적 제목 |
| description | string | 업적 설명 |
| unlockedAt | datetime | 업적 획득 시각 (ISO 8601) |

### 업적 종류

#### 스트릭 업적 (연속 풀이)
| 타입 | 제목 | 조건 |
|------|------|------|
| STREAK_3_DAYS | 3일 연속 풀이 | 3일 연속으로 문제를 해결 |
| STREAK_7_DAYS | 일주일 연속 풀이 | 7일 연속으로 문제를 해결 |
| STREAK_30_DAYS | 한 달 연속 풀이 | 30일 연속으로 문제를 해결 |

#### 총 문제 업적
| 타입 | 제목 | 조건 |
|------|------|------|
| TOTAL_10_SOLVED | 문제 10개 해결 | 총 10개의 문제를 해결 |
| TOTAL_50_SOLVED | 문제 50개 해결 | 총 50개의 문제를 해결 |
| TOTAL_100_SOLVED | 문제 100개 해결 | 총 100개의 문제를 해결 |

### 업적 획득 방식
- 정답을 맞출 때마다 자동으로 조건을 체크하여 업적을 부여합니다
- 새로 획득한 업적은 정답 응답(`POST /api/v1/games/guess`)의 `newAchievements` 필드에 포함됩니다
- 한 번에 여러 조건을 충족하면 모든 업적을 동시에 획득합니다
- 이미 획득한 업적은 중복으로 부여되지 않습니다

---

## 에러 코드 목록

### 인증 에러
| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| A001 | 401 | 이메일 또는 비밀번호가 일치하지 않습니다. |
| A002 | 401 | 유효하지 않은 토큰입니다. |

### 사용자 에러
| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| USER_002 | 409 | 이미 존재하는 이메일입니다. |
| USER_100 | 404 | 사용자를 찾을 수 없습니다. |
| VALIDATION_ERROR | 400 | 유효성 검증에 실패했습니다. |

### 게임 에러
| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| G001 | 404 | 오늘의 문제를 찾을 수 없습니다. |
| G002 | 404 | 게임 기록을 찾을 수 없습니다. |
| G003 | 409 | 이미 해결된 문제입니다. |
| G004 | 409 | 이미 포기한 문제입니다. |
| G005 | 404 | 진행 중인 게임이 없습니다. |
| G006 | 500 | 유사도 계산에 실패했습니다. |
| G007 | 400 | 없는 단어입니다. |

### 공통 에러
| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| C400 | 400 | 잘못된 요청입니다. |
| C422 | 422 | 유효성 검증에 실패했습니다. |