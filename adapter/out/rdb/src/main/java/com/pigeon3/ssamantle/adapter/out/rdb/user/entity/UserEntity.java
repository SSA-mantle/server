package com.pigeon3.ssamantle.adapter.out.rdb.user.entity;

import com.pigeon3.ssamantle.domain.model.user.User;
import com.pigeon3.ssamantle.domain.model.user.vo.Email;
import com.pigeon3.ssamantle.domain.model.user.vo.Nickname;
import com.pigeon3.ssamantle.domain.model.user.vo.Password;
import com.pigeon3.ssamantle.domain.model.user.vo.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User 영속성 엔티티 (MyBatis용)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    private Long id;
    private String email;
    private String password;
    private String role;
    private String nickname;
    private Boolean todaySolve;
    private Integer longestCont;
    private Integer nowCont;
    private Integer bestRank;
    private Boolean isDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    /**
     * 영속성 엔티티 → 도메인 모델 변환
     */
    public User toDomain() {
        return User.reconstruct(
                this.id,
                Email.of(this.email),
                Password.of(this.password),
                Role.valueOf(this.role),
                Nickname.of(this.nickname),
                this.todaySolve,
                this.longestCont,
                this.nowCont,
                this.bestRank,
                this.isDelete,
                this.createdAt,
                this.updatedAt,
                this.deletedAt
        );
    }
}
