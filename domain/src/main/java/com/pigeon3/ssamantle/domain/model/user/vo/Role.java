package com.pigeon3.ssamantle.domain.model.user.vo;

/**
 * 사용자 역할 Enum
 */
public enum Role {
    CUSTOMER("일반 사용자"),
    OWNER("소유자"),
    MANAGER("관리자"),
    MASTER("최고 관리자");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
