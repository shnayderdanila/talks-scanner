package ru.smartup.talksscanner.dto.requests;

import ru.smartup.talksscanner.domain.Sex;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Dto request for new users.
 */
public class RegisterUserDtoRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String login;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;

    @NotNull
    private Sex sex;

    @NotBlank
    private String pathLogo;
    @NotBlank
    private String password;

    public RegisterUserDtoRequest(String email, String login, String firstname, String lastname, Sex sex, String pathLogo, String password) {
        this.email = email;
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.pathLogo = pathLogo;
        this.password = password;
    }

    public RegisterUserDtoRequest(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPathLogo() {
        return pathLogo;
    }

    public void setPathLogo(String pathLogo) {
        this.pathLogo = pathLogo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterUserDtoRequest that = (RegisterUserDtoRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(login, that.login) && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && sex == that.sex && Objects.equals(pathLogo, that.pathLogo) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, login, firstname, lastname, sex, pathLogo, password);
    }

    @Override
    public String toString() {
        return "RegisterUserDtoRequest{" +
                "email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", sex=" + sex +
                ", pathLogo='" + pathLogo + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
