package com.pigeon3.ssamantle.adapter.in.http.auth.controller;

import com.pigeon3.ssamantle.adapter.in.http.auth.dto.RefreshTokenRequest;
import com.pigeon3.ssamantle.adapter.in.http.auth.dto.RefreshTokenResponseDto;
import com.pigeon3.ssamantle.adapter.in.http.auth.dto.SignInRequest;
import com.pigeon3.ssamantle.adapter.in.http.auth.dto.SignInResponseDto;
import com.pigeon3.ssamantle.adapter.in.http.common.response.ApiResponse;
import com.pigeon3.ssamantle.adapter.in.http.security.JwtProvider;
import com.pigeon3.ssamantle.application.auth.port.in.SignInCommand;
import com.pigeon3.ssamantle.application.auth.port.in.SignInResponse;
import com.pigeon3.ssamantle.application.auth.port.in.SignInUseCase;
import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 HTTP API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignInUseCase signInUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * 로그인
     * POST /api/v1/auth/sign-in
     */
    @PostMapping("/sign-in")
    public ApiResponse<SignInResponseDto> signIn(@Valid @RequestBody SignInRequest request) {
        // 1. Command 생성 및 UseCase 호출 (이메일로 사용자 조회)
        SignInCommand command = new SignInCommand(request.email());
        SignInResponse response = signInUseCase.signIn(command);

        // 2. 비밀번호 검증 (HTTP 계층에서 처리)
        if (!passwordEncoder.matches(request.password(), response.passwordHash())) {
            throw ApplicationException.of(ExceptionType.INVALID_CREDENTIALS);
        }

        // 3. JWT 토큰 생성 (HTTP 계층에서 처리)
        String accessToken = jwtProvider.createAccessToken(
                response.userId(),
                response.email(),
                response.role()
        );
        String refreshToken = jwtProvider.createRefreshToken(
                response.userId(),
                response.email(),
                response.role()
        );

        // 4. 응답 반환
        SignInResponseDto responseDto = SignInResponseDto.from(response, accessToken, refreshToken);
        return ApiResponse.success(responseDto);
    }

    /**
     * 토큰 갱신
     * POST /api/v1/auth/refresh
     */
    @PostMapping("/refresh")
    public ApiResponse<RefreshTokenResponseDto> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        // 1. Refresh token 검증
        if (!jwtProvider.validateToken(request.refreshToken())) {
            throw ApplicationException.of(ExceptionType.INVALID_TOKEN);
        }

        // 2. Refresh token에서 정보 추출
        Long userId = jwtProvider.getUserId(request.refreshToken());
        String email = jwtProvider.getEmail(request.refreshToken());
        String role = jwtProvider.getRole(request.refreshToken());

        // 3. 새로운 Access Token과 Refresh Token 발급
        String newAccessToken = jwtProvider.createAccessToken(userId, email, role);
        String newRefreshToken = jwtProvider.createRefreshToken(userId, email, role);

        // 4. 응답 반환
        RefreshTokenResponseDto responseDto = new RefreshTokenResponseDto(newAccessToken, newRefreshToken);
        return ApiResponse.success(responseDto);
    }
}
