package ru.smartup.talksscanner.test_data;

import ru.smartup.talksscanner.domain.Sex;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.RegisterUserDtoRequest;
import ru.smartup.talksscanner.dto.responses.UserDtoResponse;

public class UserTestData {
    public static User user(long id, String email, String login, String firstname, String lastname, Sex sex, String pathLogo) {
        User user = new User(email, login, firstname, lastname, sex, pathLogo);
        user.setId(id);
        return user;
    }

    public static User user() {
        return user(1,"email@mail.ru", "login", "Danila", "Danilov", Sex.MALE, "/logo.jpeg");
    }

    public static User user(long id) {
        return user(id,"email@mail.ru"+id, "login"+id, "Danila"+id, "Danilov"+id, Sex.MALE, "/logo.jpeg"+id);
    }

    public static RegisterUserDtoRequest registerUserDto(String email, String login, String firstname, String lastname, Sex sex, String pathLogo, String password) {
        return new RegisterUserDtoRequest(email, login, firstname, lastname, sex, pathLogo, password);
    }

    public static RegisterUserDtoRequest registerUserDto() {
        return registerUserDto("email@mail.ru", "login", "Danila", "Danilov", Sex.MALE, "/logo.jpeg", "password");
    }

    public static UserDtoResponse makeRegisterUserDtoResponse (long id, String email, String login, String
            firstname, String lastname,
                                                               Sex sex, String pathLogo){
        return new UserDtoResponse(id, email, login, firstname, lastname, sex, pathLogo);
    }

}
