package ru.smartup.talksscanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.RegisterUserDtoRequest;
import ru.smartup.talksscanner.dto.responses.UserDtoResponse;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.UserMapper;

/**
 * User endpoints processing service.
 */
@Service
public class UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepo    userRepo;
    private final UserMapper  userMapper;

    public UserService(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    public UserDtoResponse registerUser(RegisterUserDtoRequest registerUserDtoRequest) {
        LOGGER.info("User service: Register user with request {}", registerUserDtoRequest);
        User user = userMapper.toModel(registerUserDtoRequest);
        return userMapper.toDto(userRepo.save(user));
    }
}
