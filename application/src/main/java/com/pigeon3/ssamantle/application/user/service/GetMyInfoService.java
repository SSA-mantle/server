package com.pigeon3.ssamantle.application.user.service;

import com.pigeon3.ssamantle.application.common.exception.ApplicationException;
import com.pigeon3.ssamantle.application.common.exception.ExceptionType;
import com.pigeon3.ssamantle.application.user.port.in.GetMyInfoCommand;
import com.pigeon3.ssamantle.application.user.port.in.GetMyInfoResponse;
import com.pigeon3.ssamantle.application.user.port.in.GetMyInfoUseCase;
import com.pigeon3.ssamantle.application.user.port.out.LoadUserByIdPort;
import com.pigeon3.ssamantle.domain.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetMyInfoService implements GetMyInfoUseCase {

    private final LoadUserByIdPort loadUserByIdPort;

    public GetMyInfoService(LoadUserByIdPort loadUserByIdPort) {
        this.loadUserByIdPort = loadUserByIdPort;
    }

    @Override
    public GetMyInfoResponse execute(GetMyInfoCommand command) {
        User user = loadUserByIdPort.loadById(command.userId())
                .orElseThrow(() -> ApplicationException.of(ExceptionType.USER_NOT_FOUND));

        if (user.isDelete()) {
            throw ApplicationException.of(ExceptionType.USER_DELETED);
        }

        return GetMyInfoResponse.from(user);
    }
}
