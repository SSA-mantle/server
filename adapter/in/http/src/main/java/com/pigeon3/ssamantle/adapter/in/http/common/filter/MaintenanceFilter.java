package com.pigeon3.ssamantle.adapter.in.http.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigeon3.ssamantle.adapter.in.http.common.response.ApiResponse;
import com.pigeon3.ssamantle.application.maintenance.port.out.MaintenanceModePort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 점검(maintenance) 모드일 때 모든 API 요청을 차단하는 필터.
 *
 * - 점검 플래그는 Redis에 저장되며(MaintenanceModePort), 배치가 켜고/끄도록 한다.
 * - 필터는 가능한 앞단에서 실행되도록 HIGHEST_PRECEDENCE로 등록한다.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class MaintenanceFilter extends OncePerRequestFilter {

    private static final String ERROR_CODE_MAINTENANCE = "S503";
    private static final String DEFAULT_MESSAGE = "점검 중입니다.";

    private final MaintenanceModePort maintenanceModePort;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // API 요청만 차단 대상 (정적 리소스 등은 제외)
        return uri == null || !uri.startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!maintenanceModePort.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String message = maintenanceModePort.getReason().orElse(DEFAULT_MESSAGE);

        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(ApiResponse.fail(ERROR_CODE_MAINTENANCE, message))
        );
    }
}
