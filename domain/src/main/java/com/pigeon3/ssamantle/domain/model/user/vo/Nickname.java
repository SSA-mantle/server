package com.pigeon3.ssamantle.domain.model.user.vo;

import com.pigeon3.ssamantle.domain.model.user.exception.UserDomainException;
import com.pigeon3.ssamantle.domain.model.user.exception.UserDomainExceptionType;

import java.util.Objects;
import java.util.regex.Pattern;

import lombok.Getter;

/**
 * 닉네임 값 객체
 * 불변 객체로, 닉네임 형식 검증을 포함
 * 규칙: 최소 4자 이상, 10자 이하, 알파벳 소문자(a~z), 숫자(0~9)
 */
@Getter
public class Nickname {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-z0-9]+$");

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

        if (!NICKNAME_PATTERN.matcher(value).matches()) {
            throw UserDomainException.of(UserDomainExceptionType.INVALID_NICKNAME,
                    "닉네임은 알파벳 소문자와 숫자만 사용할 수 있습니다: " + value
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
