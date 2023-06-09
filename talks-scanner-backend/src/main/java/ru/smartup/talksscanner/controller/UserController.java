package ru.smartup.talksscanner.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.smartup.talksscanner.dto.requests.RegisterUserDtoRequest;
import ru.smartup.talksscanner.dto.responses.UserDtoResponse;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.service.AuthUserService;
import ru.smartup.talksscanner.service.UserService;

import javax.validation.Valid;

/**
 * Rest controller for user endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthUserService authUserService;

    public UserController(UserService userService, AuthUserService authUserService) {
        this.userService = userService;
        this.authUserService = authUserService;
    }

    @CrossOrigin
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDtoResponse registerUser(@RequestBody @Valid RegisterUserDtoRequest registerUserDtoRequest) {
        return userService.registerUser(registerUserDtoRequest);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDtoResponse getAuthorizedUser(@AuthenticationPrincipal UserDetails userDetails) throws NotFoundEntityException {
        return authUserService.getUserByLoginToDto(userDetails.getUsername());
    }
}
