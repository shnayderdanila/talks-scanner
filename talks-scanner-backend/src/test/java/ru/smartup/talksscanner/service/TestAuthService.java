package ru.smartup.talksscanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.smartup.talksscanner.test_data.UserTestData.user;

public class TestAuthService {


    private AuthUserService authUserService;
    private UserRepo userRepo;

    @BeforeEach
    public void init() {
        userRepo = Mockito.mock(UserRepo.class);
        authUserService = new AuthUserService(userRepo, Mockito.mock(UserMapper.class));
    }

    @Test
    public void testGetUserByLogin__and__getUserById() throws NotFoundEntityException {
        User user = user();

        Mockito.when(userRepo.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        Mockito.when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        User actualByLogin = authUserService.getUserByLogin(user.getLogin());
        User actualById = authUserService.getUserById(user.getId());

        assertEquals(user, actualById);
        assertEquals(user, actualByLogin);
    }

    @Test
    public void testGetUserByLogin__and__getUserById__userNotFound() {
        User wrongUser = user();

        Mockito.when(userRepo.findByLogin(wrongUser.getLogin())).thenReturn(Optional.empty());
        Mockito.when(userRepo.findById(wrongUser.getId())).thenReturn(Optional.empty());

        NotFoundEntityException exceptionByLogin = assertThrows(NotFoundEntityException.class, () -> authUserService.getUserByLogin(wrongUser.getLogin()));
        NotFoundEntityException exceptionById = assertThrows(NotFoundEntityException.class, () -> authUserService.getUserById(wrongUser.getId()));

        assertEquals(ErrorCode.USER_NOT_FOUND, exceptionByLogin.getErrorCode());
        assertEquals(ErrorCode.USER_NOT_FOUND, exceptionById.getErrorCode());

    }

}
