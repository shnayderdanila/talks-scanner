package ru.smartup.talksscanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.responses.UserDtoResponse;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.smartup.talksscanner.test_data.UserTestData.registerUserDto;
import static ru.smartup.talksscanner.test_data.UserTestData.user;


public class TestUserService {

    private UserService userService;
    private UserMapper userMapper;
    private UserRepo userRepo;

    @BeforeEach
    public void init() {
        userRepo = Mockito.mock(UserRepo.class);
        userMapper = new UserMapper(new BCryptPasswordEncoder());
        userService = new UserService(userRepo, userMapper);
    }

    @Test
    public void testRegisterUser() {
        User expected = user();

        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user());
        UserDtoResponse actual = userService.registerUser(registerUserDto());
        assertEquals(userMapper.toDto(expected), actual);

    }
}
