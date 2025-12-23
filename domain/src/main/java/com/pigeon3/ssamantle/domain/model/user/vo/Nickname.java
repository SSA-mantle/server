package com.pigeon3.ssamantle.domain.model.user.vo;

import com.pigeon3.ssamantle.domain.model.user.exception.UserDomainException;
import com.pigeon3.ssamantle.domain.model.user.exception.UserDomainExceptionType;

import java.util.Objects;

import lombok.Getter;

/**
 * 닉네임 값 객체
 * 불변 객체로, 닉네임 형식 검증을 포함
 * 규칙: 최소 3자 이상, 20자 이하 (모든 문자 허용)
 */
@Getter
public class Nickname {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;

    private final String value;

    private Nickname(String value) {
        this.value = value;
    }

    public static Nickname of(String value) {
        validate(value);
        return new Nickname(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw UserDomainException.of(UserDomainExceptionType.INVALID_NICKNAME, "닉네임은 필수입니다.");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw UserDomainException.of(UserDomainExceptionType.INVALID_NICKNAME,
                    String.format("닉네임은 %d자 이상 %d자 이하여야 합니다. 현재: %d자",
                            MIN_LENGTH, MAX_LENGTH, value.length())
            );
        }
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nickname nickname = (Nickname) o;
        return Objects.equals(value, nickname.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
