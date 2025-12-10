package com.pigeon3.ssamantle.domain.model.user.vo;

import com.pigeon3.ssamantle.domain.model.user.exception.InvalidPasswordException;

import java.util.Objects;
import java.util.regex.Pattern;

import lombok.Getter;

/**
 * 비밀번호 값 객체
 * 불변 객체로, 비밀번호 형식 검증을 포함
 * 규칙: 최소 8자 이상, 15자 이하, 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자
 */
@Getter
public class Password {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 15;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$"
    );

    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password of(String value) {
        validate(value);
        return new Password(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPasswordException("비밀번호는 필수입니다.");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new InvalidPasswordException(
                    String.format("비밀번호는 %d자 이상 %d자 이하여야 합니다. 현재: %d자",
                            MIN_LENGTH, MAX_LENGTH, value.length())
            );
        }

        if (!PASSWORD_PATTERN.matcher(value).matches()) {
            throw new InvalidPasswordException(
                    "비밀번호는 알파벳 대소문자, 숫자, 특수문자(@$!%*?&#)를 모두 포함해야 합니다."
            );
        }
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "****";
    }
}
