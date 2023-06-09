package ru.smartup.talksscanner.dto.responses;

import ru.smartup.talksscanner.domain.Sex;

import java.util.Objects;

/**
 * Dto response user data.
 */
public class UserDtoResponse {
    private long id;
    private String email;
    private String login;
    private String firstname;
    private String lastname;
    private Sex sex;
    private String pathLogo;

    public UserDtoResponse(long id, String email, String login, String firstname, String lastname, Sex sex, String pathLogo) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.pathLogo = pathLogo;
    }

    public UserDtoResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDtoResponse that = (UserDtoResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(login, that.login) && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && sex == that.sex && Objects.equals(pathLogo, that.pathLogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, login, firstname, lastname, sex, pathLogo);
    }
}
