-- SSAmantle Database Schema (MySQL)

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS ssamantle
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ssamantle;

-- 사용자 테이블
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '이메일',
    password VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
    role VARCHAR(50) NOT NULL COMMENT '역할 (CUSTOMER, ADMIN 등)',
    nickname VARCHAR(100) NOT NULL COMMENT '닉네임',
    today_solve BOOLEAN NOT NULL DEFAULT FALSE COMMENT '오늘 문제 풀이 여부',
    longest_cont INT NOT NULL DEFAULT 0 COMMENT '최장 연속 풀이 기록',
    now_cont INT NOT NULL DEFAULT 0 COMMENT '현재 연속 풀이 기록',
    best_rank INT NULL COMMENT '최고 등수',
    is_delete BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
    updated_at DATETIME NULL COMMENT '수정 시각',
    deleted_at DATETIME NULL COMMENT '삭제 시각',
    INDEX idx_email (email),
    INDEX idx_nickname (nickname),
    INDEX idx_is_delete (is_delete)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 테이블';

-- 문제 테이블
CREATE TABLE problems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '문제 ID',
    answer VARCHAR(255) NOT NULL COMMENT '정답',
    date DATE NOT NULL UNIQUE COMMENT '문제 날짜',
    INDEX idx_date (date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='문제 테이블';

-- 기록 테이블
CREATE TABLE records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '기록 ID',
    user_id BIGINT NOT NULL COMMENT '사용자 ID',
    problem_id BIGINT NOT NULL COMMENT '문제 ID',
    fail_count INT NOT NULL DEFAULT 0 COMMENT '실패 횟수',
    solved_at DATETIME NULL COMMENT '문제 해결 시각',
    give_up_at DATETIME NULL COMMENT '포기 시각',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
    INDEX idx_user_id (user_id),
    INDEX idx_problem_id (problem_id),
    INDEX idx_user_problem (user_id, problem_id),
    CONSTRAINT fk_records_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_records_problem FOREIGN KEY (problem_id) REFERENCES problems(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='문제 풀이 기록 테이블';

-- 사용자 업적 테이블
CREATE TABLE user_achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '업적 ID',
    user_id BIGINT NOT NULL COMMENT '사용자 ID',
    achievement_type VARCHAR(50) NOT NULL COMMENT '업적 타입',
    unlocked_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '획득 시각',
    INDEX idx_user_id (user_id),
    INDEX idx_achievement_type (achievement_type),
    UNIQUE KEY uk_user_achievement (user_id, achievement_type),
    CONSTRAINT fk_user_achievements_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자 업적 테이블';
