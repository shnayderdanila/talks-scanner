package ru.smartup.talksscanner.dto.responses;

import java.util.Objects;

public class RateDtoResponse {
    private long id;
    private String authorFirstName;
    private String authorLastName;
    private int rate;

    public RateDtoResponse(long id, String authorFirstName, String authorLastName, int rate) {
        this.id = id;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.rate = rate;
    }

    public RateDtoResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateDtoResponse that = (RateDtoResponse) o;
        return getId() == that.getId() && getRate() == that.getRate() && Objects.equals(getAuthorFirstName(), that.getAuthorFirstName()) && Objects.equals(getAuthorLastName(), that.getAuthorLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAuthorFirstName(), getAuthorLastName(), getRate());
    }
}
