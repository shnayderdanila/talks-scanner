package ru.smartup.talksscanner.dto.requests;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * Dto to insert/update Idea.
 */
public class SaveIdeaDtoRequest implements Serializable {

    @NotBlank
    private String title;
    @NotBlank
    private String description;

    public SaveIdeaDtoRequest() {
    }

    public SaveIdeaDtoRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        if (!(o instanceof SaveIdeaDtoRequest request)) return false;
        return Objects.equals(getTitle(), request.getTitle()) && Objects.equals(getDescription(), request.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription());
    }

    @Override
    public String toString() {
        return "UpdateIdeaDtoRequest{" +
                " title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
