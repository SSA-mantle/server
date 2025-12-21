package com.pigeon3.ssamantle.application.user.port.out;

/**
 * 모든 사용자에 대해 일자 변경 시 처리되는 상태를 일괄 초기화하는 포트.
 *
 * 도메인 User.resetTodaySolve()와 동일한 효과를 DB에서 한 번의 UPDATE로 수행한다.
 */
public interface ResetTodaySolveForAllUsersPort {

    /**
     * 모든 사용자의 todaySolve/nowCont 등을 일괄 초기화한다.
     */
    void resetTodaySolveForAll();
}
