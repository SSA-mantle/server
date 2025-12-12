package com.pigeon3.ssamantle.adapter.in.http.user.controller;

import com.pigeon3.ssamantle.adapter.in.http.common.response.ApiResponse;
import com.pigeon3.ssamantle.adapter.in.http.user.dto.SignUpRequest;
import com.pigeon3.ssamantle.adapter.in.http.user.dto.SignUpResponseDto;
import com.pigeon3.ssamantle.application.user.port.in.SignUpResponse;
import com.pigeon3.ssamantle.application.user.port.in.SignUpUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
