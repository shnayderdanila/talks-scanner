package ru.smartup.talksscanner.dto.requests;

import ru.smartup.talksscanner.domain.TopicStatus;

import java.util.Objects;

/**
 * DTO request parameters to get Topics.
 * */
public class GetAllTopicsParamRequest {
    private String tags;
    private TopicStatus status;

    public GetAllTopicsParamRequest() {
    }

    public GetAllTopicsParamRequest(String tags, TopicStatus phase) {
        this.tags = tags;
        this.status = phase;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public TopicStatus getStatus() {
        return status;
    }

    public void setStatus(TopicStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GetAllTopicsParamRequest{" +
                "tags='" + tags + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetAllTopicsParamRequest that)) return false;
        return Objects.equals(getTags(), that.getTags()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTags(), getStatus());
    }
}
