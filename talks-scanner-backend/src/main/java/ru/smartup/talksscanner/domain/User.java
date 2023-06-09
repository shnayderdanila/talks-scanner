package ru.smartup.talksscanner.domain;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_user"
    )
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String login;
    private String firstname;
    private String lastname;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(name = "path_logo")
    private String pathLogo;
    @Column(name = "encoded_password")
    private String encodedPassword;

    public User() {
    }

    public User(String email, String login, String firstname, String lastname, Sex sex, String pathLogo) {
        this.email = email;
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.pathLogo = pathLogo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getLogin(), user.getLogin()) && Objects.equals(getFirstname(), user.getFirstname()) && Objects.equals(getLastname(), user.getLastname()) && getSex() == user.getSex() && Objects.equals(getPathLogo(), user.getPathLogo()) && Objects.equals(getEncodedPassword(), user.getEncodedPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getLogin(), getFirstname(), getLastname(), getSex(), getPathLogo(), getEncodedPassword());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", sex=" + sex +
                ", pathLogo='" + pathLogo + '\'' +
                ", encodedPassword='" + encodedPassword + '\'' +
                '}';
    }

}
