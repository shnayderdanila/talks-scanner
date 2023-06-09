package ru.smartup.talksscanner.dto.requests;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * DTO request for new user topics.
 */
public class InsertTopicDtoRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String description;

    public InsertTopicDtoRequest(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public InsertTopicDtoRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsertTopicDtoRequest that = (InsertTopicDtoRequest) o;
        return Objects.equals(title, that.title) && Objects.equals(author, that.author) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, description);
    }

    @Override
    public String toString() {
        return "InsertTopicDtoRequest{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
