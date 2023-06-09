package ru.smartup.talksscanner.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class UpdateScheduledTopicDtoRequest implements UpdateTopicDtoRequest {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;

    public UpdateScheduledTopicDtoRequest(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public UpdateScheduledTopicDtoRequest() {}

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateScheduledTopicDtoRequest that = (UpdateScheduledTopicDtoRequest) o;
        return Objects.equals(getEventDate(), that.getEventDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventDate());
    }
}
