package ru.smartup.talksscanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.responses.UserDtoResponse;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.UserMapper;

/**
 * Service get User from database.
 */
@Service
public class AuthUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserService.class);
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public AuthUserService(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    public User getUserById(Long id) throws NotFoundEntityException {
        LOGGER.debug("get User by id : {}", id);
        return userRepo.findById(id).orElseThrow(
                () -> new NotFoundEntityException(
                        ErrorCode.USER_NOT_FOUND,
                        String.format(ErrorCode.USER_NOT_FOUND.getTemplate(), id)
                )
        );
    }

    public User getUserByLogin(String login) throws NotFoundEntityException {
        LOGGER.debug("get User by login : {}", login);
        return userRepo.findByLogin(login).orElseThrow(
                () -> new NotFoundEntityException(
                        ErrorCode.USER_NOT_FOUND,
                        String.format(ErrorCode.USER_NOT_FOUND.getTemplate(), login)
                )
        );
    }

    public UserDtoResponse getUserByLoginToDto(String login) throws NotFoundEntityException {
        return userMapper.toDto(getUserByLogin(login));
    }
}
