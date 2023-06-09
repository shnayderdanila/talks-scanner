package ru.smartup.talksscanner.dto.requests;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class UpdateAnnouncedTopicDtoRequest implements UpdateTopicDtoRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;

    public UpdateAnnouncedTopicDtoRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public UpdateAnnouncedTopicDtoRequest() {
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
        if (o == null || getClass() != o.getClass()) return false;
        UpdateAnnouncedTopicDtoRequest that = (UpdateAnnouncedTopicDtoRequest) o;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription());
    }
}
