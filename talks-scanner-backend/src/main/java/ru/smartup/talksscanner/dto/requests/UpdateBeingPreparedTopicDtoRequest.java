package ru.smartup.talksscanner.dto.requests;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class UpdateBeingPreparedTopicDtoRequest implements UpdateTopicDtoRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String tags;

    public UpdateBeingPreparedTopicDtoRequest(String title, String description, String tags) {
        this.title = title;
        this.description = description;
        this.tags = tags;
    }

    public UpdateBeingPreparedTopicDtoRequest() {}

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateBeingPreparedTopicDtoRequest that = (UpdateBeingPreparedTopicDtoRequest) o;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription(), getTags());
    }
}
