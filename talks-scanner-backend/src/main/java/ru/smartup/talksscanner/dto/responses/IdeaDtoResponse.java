package ru.smartup.talksscanner.dto.responses;

import ru.smartup.talksscanner.domain.IdeaStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Dto to response Idea
 */
public class IdeaDtoResponse implements Serializable {
    private Long id;
    private String title;
    private String description;

    private Double rate;
    private final UserSimpleDtoResponse user;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
    private IdeaStatus status;

    public IdeaDtoResponse() {
        this.user = new UserSimpleDtoResponse();
    }

    public IdeaDtoResponse(Long id, String title, String description, double rate, UserSimpleDtoResponse user, LocalDateTime creationDate, LocalDateTime lastUpdateDate, IdeaStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rate = rate;
        this.user = user;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserId(long userId) {
        this.user.setUserId(userId);
    }

    public void setUserFirstName(String firstName) {
        this.user.setFirstname(firstName);
    }

    public void setUserLastName(String lastName) {
        this.user.setLastname(lastName);
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setStatus(IdeaStatus status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public UserSimpleDtoResponse getUser() {
        return user;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public IdeaStatus getStatus() {
        return status;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getRate() {
        return rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdeaDtoResponse response)) return false;
        return Objects.equals(getId(), response.getId()) && Objects.equals(getTitle(), response.getTitle()) && Objects.equals(getDescription(), response.getDescription()) && Objects.equals(getUser(), response.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getUser(), getStatus());
    }
}
