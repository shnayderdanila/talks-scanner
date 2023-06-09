package ru.smartup.talksscanner.dto.responses;

import java.util.Objects;

/**
 * Dto response for comments.
 */
public class CommentDtoResponse {

    private long id;
    private String authorFirstName;
    private String authorLastName;
    private String text;
    private long userId;

    public CommentDtoResponse(long id, String authorFirstName, String authorLastName, String text, long userId) {
        this.id = id;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.text = text;
        this.userId = userId;
    }

    public CommentDtoResponse() {
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDtoResponse response = (CommentDtoResponse) o;
        return getId() == response.getId() && getUserId() == response.getUserId() && Objects.equals(getAuthorFirstName(), response.getAuthorFirstName()) && Objects.equals(getAuthorLastName(), response.getAuthorLastName()) && Objects.equals(getText(), response.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getAuthorFirstName(), getAuthorLastName(), getText());
    }
}
