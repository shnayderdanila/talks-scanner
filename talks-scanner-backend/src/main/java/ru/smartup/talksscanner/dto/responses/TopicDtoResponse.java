package ru.smartup.talksscanner.dto.responses;

import ru.smartup.talksscanner.domain.TopicStatus;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Dto response for topics.
 */
public class TopicDtoResponse {
    private long id;
    private String title;
    private String author;
    private String description;
    private String tags;
    private LocalDateTime eventDate;
    private String presentationLink;
    private String videoLink;
    private TopicStatus status;
    private long userId;
    private Double rate;

    public TopicDtoResponse(long id, String title, String author, String description, String tags, LocalDateTime eventDate, String presentationLink, String videoLink, TopicStatus status, long userId, Double rate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.tags = tags;
        this.eventDate = eventDate;
        this.presentationLink = presentationLink;
        this.videoLink = videoLink;
        this.status = status;
        this.userId = userId;
        this.rate = rate;
    }

    public TopicDtoResponse() {}

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

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

    public TopicStatus getStatus() {
        return status;
    }

    public void setStatus(TopicStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicDtoResponse that = (TopicDtoResponse) o;
        return id == that.id && Objects.equals(rate, that.rate) && userId == that.userId && Objects.equals(title, that.title) && Objects.equals(author, that.author) && Objects.equals(description, that.description) && Objects.equals(tags, that.tags) && Objects.equals(eventDate, that.eventDate) && Objects.equals(presentationLink, that.presentationLink) && Objects.equals(videoLink, that.videoLink) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rate, title, author, description, tags, eventDate, presentationLink, videoLink, status, userId);
    }
}
