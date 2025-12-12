package com.pigeon3.ssamantle.adapter.in.http.user.controller;

import com.pigeon3.ssamantle.adapter.in.http.common.response.ApiResponse;
import com.pigeon3.ssamantle.adapter.in.http.user.dto.SignUpRequest;
import com.pigeon3.ssamantle.adapter.in.http.user.dto.SignUpResponseDto;
import com.pigeon3.ssamantle.adapter.in.http.user.dto.UpdateUserRequest;
import com.pigeon3.ssamantle.adapter.in.http.user.dto.UpdateUserResponseDto;
import com.pigeon3.ssamantle.application.user.port.in.SignUpResponse;
import com.pigeon3.ssamantle.application.user.port.in.SignUpUseCase;
import com.pigeon3.ssamantle.application.user.port.in.UpdateUserResponse;
import com.pigeon3.ssamantle.application.user.port.in.UpdateUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관련 HTTP API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final SignUpUseCase signUpUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * POST /api/v1/users
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequest request) {
        // 1. 비밀번호 암호화 (HTTP 계층에서 처리)
        String encodedPassword = passwordEncoder.encode(request.password());

        // 2. Command 생성 (암호화된 비밀번호 사용)
        SignUpResponse response = signUpUseCase.execute(request.toCommand(encodedPassword));

        // 3. 응답 반환
        SignUpResponseDto responseDto = SignUpResponseDto.from(response);
        return ApiResponse.success(responseDto);
    }

    /**
     * 내 정보 수정
     * PATCH /api/v1/users/me
     */
    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UpdateUserResponseDto> updateMyInfo(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateUserRequest request) {
        // 1. 비밀번호 암호화 (비밀번호가 있는 경우에만)
        String encodedPassword = null;
        if (request.password() != null && !request.password().isBlank()) {
            encodedPassword = passwordEncoder.encode(request.password());
        }

        // 2. Command 생성 및 실행
        UpdateUserResponse response = updateUserUseCase.execute(request.toCommand(userId, encodedPassword));

        // 3. 응답 반환
        UpdateUserResponseDto responseDto = UpdateUserResponseDto.from(response);
        return ApiResponse.success(responseDto);
    }
}
