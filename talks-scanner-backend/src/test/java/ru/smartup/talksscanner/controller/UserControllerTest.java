package ru.smartup.talksscanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.smartup.talksscanner.config.ApplicationUserService;
import ru.smartup.talksscanner.config.WebSecurityConfiguration;
import ru.smartup.talksscanner.domain.Sex;
import ru.smartup.talksscanner.dto.requests.RegisterUserDtoRequest;
import ru.smartup.talksscanner.dto.responses.UserDtoResponse;
import ru.smartup.talksscanner.handler.GlobalErrorHandler;
import ru.smartup.talksscanner.service.AuthUserService;
import ru.smartup.talksscanner.service.UserService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Import(WebSecurityConfiguration.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ApplicationUserService applicationUserService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthUserService authUserService;
    @Autowired
    private ObjectMapper mapper;

    private final RegisterUserDtoRequest request = new RegisterUserDtoRequest("email", "login", "firstName", "lastName", Sex.MALE, "pathLogo", "password");
    private final UserDtoResponse expected = new UserDtoResponse(1L, "email", "login", "firstName", "lastName", Sex.MALE, "pathLogo");


    @Test
    public void testRegisterUser() throws Exception {
        when(userService.registerUser(request)).thenReturn(expected);

        MvcResult result = post_registerUser(request, status().isOk());

        assertEquals(expected, mapper.readValue(result.getResponse().getContentAsString(), UserDtoResponse.class));
    }

    @ParameterizedTest
    @MethodSource("makeWrongRegisterUserDtoRequest")
    public void testRegisterUser_wrongData(RegisterUserDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(post_registerUser(request, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testLoginUser() throws Exception {
        when(authUserService.getUserByLoginToDto(expected.getLogin())).thenReturn(expected);
        MvcResult result = get_getUserByLogin(status().isOk());
        assertEquals(expected, mapper.readValue(result.getResponse().getContentAsString(), UserDtoResponse.class));
    }

    @Test
    public void testLoginUser__unauthorizedUser() throws Exception {
        get_getUserByLogin(status().isUnauthorized());
    }

    private MvcResult post_registerUser(RegisterUserDtoRequest request, ResultMatcher status) throws Exception{
        return mvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult get_getUserByLogin(ResultMatcher status) throws Exception{
        return mvc.perform(get("/api/v1/users"))
                .andExpect(status)
                .andReturn();
    }

    private static Stream<Arguments> makeWrongRegisterUserDtoRequest(){
        return Stream.of(Arguments.arguments(new RegisterUserDtoRequest("", "login", "firstname", "lastname", Sex.MALE, "pathLogo", "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", "", "firstname", "lastname", Sex.MALE, "pathLogo", "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", "login", "", "lastname", Sex.MALE, "pathLogo", "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", "login", "firstname", "", Sex.MALE, "pathLogo", "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", "login", "firstname", "lastname", Sex.MALE, "", "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", "login", "firstname", "lastname", Sex.MALE, "pathLogo", "")),
                Arguments.arguments(new RegisterUserDtoRequest(null, "login", "firstname", "lastname", Sex.MALE, "pathLogo", "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", null, "firstname", "lastname", Sex.MALE, "pathLogo", "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", "login", null, "lastname", Sex.MALE, "pathLogo", "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", "login", "firstname", null, Sex.MALE, "pathLogo", "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", "login", "firstname", "lastname", Sex.MALE, null, "password")),
                Arguments.arguments(new RegisterUserDtoRequest("email", "login", "firstname", "lastname", Sex.MALE, "pathLogo", null)));
    }
}