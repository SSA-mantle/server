# 싸멘틀 (SSAmantle) 프로젝트

싸피 + 세멘틀(유사도 기반 단어 추론 게임)을 결합한 프로젝트입니다.

## 아키텍처

### 멀티모듈 헥사고날 아키텍처

```
ssamatle-server/
├── domain/              # 도메인 계층 (핵심 비즈니스 로직)
├── application/         # 애플리케이션 계층 (Use Case)
├── adapter/            # 어댑터 계층
│   ├── in/
│   │   └── http/       # 인바운드 어댑터 (REST API)
│   └── out/
│       ├── rdb/        # 아웃바운드 어댑터 (RDB)
│       ├── nosql/      # 아웃바운드 어댑터 (NoSQL)
│       └── in-memory/  # 아웃바운드 어댑터 (In-Memory)
└── bootstrap/          # 애플리케이션 부트스트랩
```

## 개발 원칙

### DDD (Domain-Driven Design)

#### 1. 도메인 계층 (domain)
- **엔티티(Entity)**: 고유 식별자를 가진 도메인 객체
- **값 객체(Value Object)**: 불변 객체, 동등성 비교
- **애그리거트(Aggregate)**: 관련된 객체들의 묶음, 일관성 경계
- **도메인 서비스(Domain Service)**: 엔티티나 값 객체에 속하지 않는 도메인 로직
- **도메인 이벤트(Domain Event)**: 도메인에서 발생한 중요한 사건

**규칙**:
- 다른 계층에 대한 의존성 없음 (순수 Java)
- 비즈니스 규칙과 로직은 도메인에만 존재
- 풍부한 도메인 모델(Rich Domain Model) 지향
- 기술적 관심사(DB, HTTP 등) 배제

#### 2. 애플리케이션 계층 (application)
- **인바운드 포트 (UseCase 인터페이스)**: 애플리케이션의 진입점
  - `~UseCase` 명명 (예: `CreateGameUseCase`)
  - 인바운드 어댑터(Controller)가 호출하는 인터페이스
- **UseCase 구현체 (Service)**: 인바운드 포트 구현
  - `~Service` 명명 (예: `CreateGameService`)
  - `~UseCase` 인터페이스를 implements
  - 도메인 로직 오케스트레이션
- **아웃바운드 포트**: 외부 시스템과의 통신 인터페이스
  - `~Port` 명명 (예: `LoadGamePort`, `SaveGamePort`)
  - 영속성, 외부 API 등의 추상화

**규칙**:
- 도메인 계층에만 의존
- 트랜잭션 경계 관리
- 어댑터 구현체는 알 수 없음 (포트만 사용)
- 비즈니스 로직은 도메인에 위임

**호출 흐름**:
```
[Controller]
    → (calls) [UseCase 인터페이스]
    ← (implemented by) [Service]
    → (calls) [아웃바운드 Port 인터페이스]
    ← (implemented by) [아웃바운드 Adapter]
```

#### 3. 어댑터 계층 (adapter)

##### 인바운드 어댑터 (adapter/in)
- **HTTP 어댑터**: REST API 컨트롤러
- `~UseCase` 인터페이스(인바운드 포트)를 주입받아 호출
- 요청/응답 DTO 변환

**규칙**:
- `~UseCase` 인터페이스만 의존 (구현체인 Service는 모름)
- 웹 관련 로직만 포함 (검증, 변환)
- 도메인 로직 포함 금지

**예시**:
```java
@RestController
public class GameController {
    private final CreateGameUseCase createGameUseCase;  // 인바운드 포트

    public GameController(CreateGameUseCase createGameUseCase) {
        this.createGameUseCase = createGameUseCase;
    }

    @PostMapping("/games")
    public ResponseDto createGame(@RequestBody RequestDto dto) {
        return createGameUseCase.create(dto);  // UseCase 호출
    }
}
```

##### 아웃바운드 어댑터 (adapter/out)
- **RDB 어댑터**: JPA/MyBatis 등을 사용한 영속성
- **NoSQL 어댑터**: Redis, MongoDB 등
- **In-Memory 어댑터**: 캐싱, 임시 저장소

**규칙**:
- 아웃바운드 포트 구현
- 도메인 모델 ↔ 영속성 모델 변환
- 기술 구현 세부사항 캡슐화

#### 4. 부트스트랩 계층 (bootstrap)
- Spring Boot 애플리케이션 시작점
- 의존성 주입 설정
- 모든 모듈 조합

### 의존성 규칙

```
bootstrap → adapter → application → domain
```

- **domain**: 의존성 없음
- **application**: domain만 의존
- **adapter**: application, domain 의존
- **bootstrap**: 모든 모듈 의존

### 명명 규칙

#### 패키지 구조
```
domain/
└── com.pigeon3.ssamantle.domain.model.{bounded-context}/
    ├── {DomainModel}.java  # 도메인 모델 (예: User.java, Problem.java)
    ├── vo/                 # 값 객체
    ├── service/            # 도메인 서비스
    ├── event/              # 도메인 이벤트
    └── exception/          # 도메인 예외

application/
└── com.pigeon3.ssamantle.application.{bounded-context}/
    ├── port/
    │   ├── in/        # UseCase 인터페이스
    │   └── out/       # Repository, ExternalService 인터페이스
    └── service/       # UseCase 구현체

adapter/in/http/
└── com.pigeon3.ssamantle.adapter.in.http.{bounded-context}/
    ├── controller/
    ├── dto/
    └── mapper/

adapter/out/{rdb|nosql|in-memory}/
└── com.pigeon3.ssamantle.adapter.out.{infrastructure}.{bounded-context}/
    ├── entity/        # 영속성 엔티티 (예: UserEntity.java)
    ├── repository/    # Repository 구현체
    └── mapper/        # 도메인 모델 ↔ 영속성 엔티티 변환
```

#### 클래스 명명
- **인바운드 포트 (UseCase 인터페이스)**: `~UseCase`
  - 예: `CreateGameUseCase`
  - 위치: `application/port/in/`
- **UseCase 구현체 (Service)**: `~Service`
  - 예: `CreateGameService implements CreateGameUseCase`
  - 위치: `application/service/`
- **아웃바운드 포트**: `~Port`
  - 예: `LoadGamePort`, `SaveGamePort`
  - 위치: `application/port/out/`
- **아웃바운드 어댑터**: `~Adapter`
  - 예: `GamePersistenceAdapter implements SaveGamePort, LoadGamePort`
  - 위치: `adapter/out/{rdb|nosql|in-memory}/`
- **인바운드 어댑터**: `~Controller`
  - 예: `GameController` (의존: `CreateGameUseCase`)
  - 위치: `adapter/in/http/`
- **도메인 서비스**: `~DomainService`
  - 예: `SimilarityCalculationDomainService`

#### 전체 구조 예시
```
// 1. 인바운드 포트 정의 (application/port/in/)
interface CreateGameUseCase {
    GameResponse create(GameRequest request);
}

// 2. UseCase 구현 (application/service/)
@Service
class CreateGameService implements CreateGameUseCase {
    private final SaveGamePort saveGamePort;  // 아웃바운드 포트
    private final GameDomainService gameDomainService;  // 도메인 서비스

    public GameResponse create(GameRequest request) {
        Game game = gameDomainService.createNewGame(request);
        saveGamePort.save(game);
        return GameResponse.from(game);
    }
}

// 3. 인바운드 어댑터 (adapter/in/http/)
@RestController
class GameController {
    private final CreateGameUseCase createGameUseCase;  // 인바운드 포트

    @PostMapping("/games")
    public GameResponse create(@RequestBody GameRequest request) {
        return createGameUseCase.create(request);
    }
}

// 4. 아웃바운드 포트 정의 (application/port/out/)
interface SaveGamePort {
    void save(Game game);
}

// 5. 아웃바운드 어댑터 (adapter/out/rdb/)
@Component
class GamePersistenceAdapter implements SaveGamePort {
    private final GameJpaRepository repository;

    public void save(Game game) {
        GameEntity entity = GameMapper.toEntity(game);
        repository.save(entity);
    }
}
```

### 코드 작성 가이드

#### 새로운 기능 추가 순서
1. **도메인 모델 설계** (domain/)
   - Entity, Value Object, Domain Service 작성
   - 비즈니스 규칙 구현

2. **인바운드 포트 정의** (application/port/in/)
   - `~UseCase` 인터페이스 작성
   - 입력/출력 DTO 정의

3. **아웃바운드 포트 정의** (application/port/out/)
   - `~Port` 인터페이스 작성 (필요시)
   - 영속성, 외부 API 등의 추상화

4. **UseCase 구현** (application/service/)
   - `~Service` 클래스가 `~UseCase` implements
   - 도메인 로직 오케스트레이션
   - 아웃바운드 포트 호출

5. **아웃바운드 어댑터 구현** (adapter/out/)
   - `~Adapter` 클래스가 아웃바운드 포트 implements
   - 영속성 엔티티 매핑

6. **인바운드 어댑터 구현** (adapter/in/http/)
   - Controller에서 `~UseCase` 주입
   - HTTP 요청/응답 처리

#### 항상 확인할 것
- 의존성 방향: `adapter → application → domain`
- 비즈니스 로직이 도메인에 있는가?
- Controller는 UseCase 인터페이스만 의존하는가?
- Service는 아웃바운드 Port 인터페이스만 의존하는가?

#### 금지 사항
- 도메인에서 프레임워크 의존성 사용 ❌
- 어댑터에서 비즈니스 로직 작성 ❌
- Controller가 Service 구현체 직접 의존 ❌
- 계층 간 직접 참조 (포트 없이) ❌

#### 예외 처리 규칙

**반드시 정의된 예외를 사용합니다.**

- `IllegalArgumentException`, `RuntimeException` 등 기본 예외를 직접 던지지 않습니다 ❌
- 각 계층별로 명확한 의미를 가진 커스텀 예외를 정의하여 사용합니다 ✅

**예외 계층 구조**:
```
domain/
└── com.pigeon3.ssamatle.domain.model.{bounded-context}/
    └── exception/
        ├── {Domain}Exception.java          # 도메인 예외 기본 클래스
        ├── InvalidEmailException.java      # 구체적 도메인 예외
        ├── InvalidPasswordException.java
        └── InvalidNicknameException.java
```

**예외 명명 규칙**:
- 도메인 예외: `{Domain}Exception`, `Invalid{Field}Exception`
- 애플리케이션 예외: `{UseCase}Exception`
- 인프라 예외: `{Infrastructure}Exception`

**예시**:
```java
// ❌ 나쁜 예
public static Email of(String value) {
    if (!isValid(value)) {
        throw new IllegalArgumentException("잘못된 이메일입니다.");
    }
    return new Email(value);
}

// ✅ 좋은 예
public static Email of(String value) {
    if (!isValid(value)) {
        throw new InvalidEmailException("잘못된 이메일입니다: " + value);
    }
    return new Email(value);
}
```

### 기술 스택
- Language: Java
- Framework: Spring Boot
- Build: Gradle (멀티모듈)
- Persistence: MyBatis (RDB), Redis (NoSQL)
