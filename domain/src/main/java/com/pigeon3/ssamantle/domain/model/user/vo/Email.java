package com.pigeon3.ssamantle.domain.model.user.vo;

import com.pigeon3.ssamantle.domain.model.user.exception.UserDomainException;
import com.pigeon3.ssamantle.domain.model.user.exception.UserDomainExceptionType;

import java.util.Objects;
import java.util.regex.Pattern;

import lombok.Getter;

/**
 * 이메일 값 객체
 * 불변 객체로, 이메일 형식 검증을 포함
 */
@Getter
public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email of(String value) {
        validate(value);
        return new Email(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw UserDomainException.of(UserDomainExceptionType.INVALID_EMAIL, "이메일은 필수입니다.");
        }

        if (value.length() > 255) {
            throw UserDomainException.of(UserDomainExceptionType.INVALID_EMAIL, "이메일은 255자를 초과할 수 없습니다: " + value.length() + "자");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw UserDomainException.of(UserDomainExceptionType.INVALID_EMAIL, "올바른 이메일 형식이 아닙니다: " + value);
        }
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
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
