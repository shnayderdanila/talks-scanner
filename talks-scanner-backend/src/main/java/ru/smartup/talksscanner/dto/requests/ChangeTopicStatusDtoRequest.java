package ru.smartup.talksscanner.dto.requests;

import ru.smartup.talksscanner.domain.TopicStatus;

public class ChangeTopicStatusDtoRequest {

    private TopicStatus status;

    public ChangeTopicStatusDtoRequest() {
    }

    public ChangeTopicStatusDtoRequest(TopicStatus status) {
        this.status = status;
    }

    public TopicStatus getStatus() {
        return status;
    }

    public void setStatus(TopicStatus status) {
        this.status = status;
    }
}
