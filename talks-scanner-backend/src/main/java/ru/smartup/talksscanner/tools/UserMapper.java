package ru.smartup.talksscanner.tools;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.RegisterUserDtoRequest;
import ru.smartup.talksscanner.dto.responses.UserDtoResponse;

/**
 * Converts user model to dto and vice versa.
 */
@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toModel(RegisterUserDtoRequest request){
        User user = new User();

        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setSex(request.getSex());
        user.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
        user.setPathLogo(request.getPathLogo());

        return user;
    }

    public UserDtoResponse toDto(User user){
        UserDtoResponse response = new UserDtoResponse();

        response.setEmail(user.getEmail());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setId(user.getId());
        response.setLogin(user.getLogin());
        response.setPathLogo(user.getPathLogo());
        response.setSex(user.getSex());

        return response;
    }
}
