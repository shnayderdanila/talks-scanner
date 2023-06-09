package ru.smartup.talksscanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.smartup.talksscanner.domain.Sex;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.RegisterUserDtoRequest;
import ru.smartup.talksscanner.dto.responses.UserDtoResponse;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.smartup.talksscanner.test_data.UserTestData.user;

class UserServiceTest {
    private UserRepo userRepo;
    private UserService userService;

    @BeforeEach
    public void setUp(){
        userRepo = mock(UserRepo.class);
        userService = new UserService(userRepo, new UserMapper(new BCryptPasswordEncoder()));
    }

    @Test
    public void testRegisterUser(){
        User savedUser = user(1 ,"email", "login", "firstName", "lastName", Sex.MALE, "pathLogo");

        when(userRepo.save(Mockito.any(User.class))).thenReturn(savedUser);

        RegisterUserDtoRequest request = new RegisterUserDtoRequest("email", "login", "firstName", "lastName", Sex.MALE, "pathLogo", "password");

        UserDtoResponse expected = new UserDtoResponse(1L, "email", "login", "firstName", "lastName", Sex.MALE, "pathLogo");
        UserDtoResponse actual = userService.registerUser(request);
        assertEquals(expected, actual);
    }
}