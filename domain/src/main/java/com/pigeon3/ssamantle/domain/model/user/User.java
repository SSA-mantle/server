package com.pigeon3.ssamantle.domain.model.user;

import com.pigeon3.ssamantle.domain.model.user.vo.Email;
import com.pigeon3.ssamantle.domain.model.user.vo.Nickname;
import com.pigeon3.ssamantle.domain.model.user.vo.Password;
import com.pigeon3.ssamantle.domain.model.user.vo.Role;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Getter;

/**
 * 사용자 도메인 모델
 * 고유 식별자를 가진 도메인 객체
 */
@Getter
public class User {
    private final Long id;
    private final Email email;
    private Password password;
    private Role role;
    private Nickname nickname;
    private boolean todaySolve;
    private int longestCont;
    private int nowCont;
    private boolean isDelete;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private User(Long id, Email email, Password password, Role role, Nickname nickname,
                 boolean todaySolve, int longestCont, int nowCont, boolean isDelete,
                 LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.todaySolve = todaySolve;
        this.longestCont = longestCont;
        this.nowCont = nowCont;
        this.isDelete = isDelete;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 새로운 사용자 생성 (회원가입)
     */
    public static User create(Email email, Password password, Nickname nickname) {
        return new User(
                null,
                email,
                password,
                Role.CUSTOMER,
                nickname,
                false,
                0,
                0,
                false,
                LocalDateTime.now(),
                null,
                null
        );
    }

    /**
     * 기존 사용자 재구성 (영속성 계층에서 사용)
     */
    public static User reconstruct(Long id, Email email, Password password, Role role, Nickname nickname,
                                    boolean todaySolve, int longestCont, int nowCont, boolean isDelete,
                                    LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new User(id, email, password, role, nickname, todaySolve, longestCont, nowCont, isDelete,
                createdAt, updatedAt, deletedAt);
    }

    /**
     * 문제 해결 성공 시 호출
     */
    public void solveProblem() {
        this.todaySolve = true;
        this.nowCont++;
        if (this.nowCont > this.longestCont) {
            this.longestCont = this.nowCont;
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 연속 풀이 기록 초기화 (일자가 지나면 호출)
     */
    public void resetTodaySolve() {
        if (!this.todaySolve) {
            this.nowCont = 0;
        }
        this.todaySolve = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 비밀번호 변경
     */
    public void changePassword(Password newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 닉네임 변경
     */
    public void changeNickname(Nickname newNickname) {
        this.nickname = newNickname;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 역할 변경
     */
    public void changeRole(Role newRole) {
        this.role = newRole;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 논리적 삭제
     */
    public void delete() {
        this.isDelete = true;
        this.deletedAt = LocalDateTime.now();
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", role=" + role +
                ", nickname=" + nickname +
                ", todaySolve=" + todaySolve +
                ", longestCont=" + longestCont +
                ", nowCont=" + nowCont +
                ", isDelete=" + isDelete +
                '}';
    }
}
