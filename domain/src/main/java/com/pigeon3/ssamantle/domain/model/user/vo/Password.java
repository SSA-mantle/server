package com.pigeon3.ssamantle.domain.model.user.vo;

import java.util.Objects;

import lombok.Getter;

/**
 * 비밀번호 값 객체
 * 불변 객체로, 암호화된 비밀번호를 보관
 * 검증은 HTTP 계층에서 수행
 */
@Getter
public class Password {
    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password of(String value) {
        return new Password(value);
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
