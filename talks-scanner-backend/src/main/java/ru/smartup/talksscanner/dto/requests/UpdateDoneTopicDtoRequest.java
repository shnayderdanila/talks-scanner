package ru.smartup.talksscanner.dto.requests;

import javax.validation.constraints.NotBlank;
import java.util.Objects;


public class UpdateDoneTopicDtoRequest implements UpdateTopicDtoRequest {

    @NotBlank
    private String presentationLink;
    @NotBlank
    private String videoLink;

    public UpdateDoneTopicDtoRequest(String presentationLink, String videoLink) {
        this.presentationLink = presentationLink;
        this.videoLink = videoLink;
    }

    public UpdateDoneTopicDtoRequest() {}

    public String getPresentationLink() {
        return presentationLink;
    }

    public void setPresentationLink(String presentationLink) {
        this.presentationLink = presentationLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateDoneTopicDtoRequest that = (UpdateDoneTopicDtoRequest) o;
        return Objects.equals(getPresentationLink(), that.getPresentationLink()) && Objects.equals(getVideoLink(), that.getVideoLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPresentationLink(), getVideoLink());
    }
}
