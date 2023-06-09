package ru.smartup.talksscanner.dto.requests;

import java.util.Objects;

/**
 * DTO request parameters to get Ideas.
 * */
public class GetAllIdeasParamRequest {

    private String titleStartsWith;

    private String descriptionStartsWith;

    public GetAllIdeasParamRequest() {
    }

    public GetAllIdeasParamRequest(String titleStartWith, String descriptionStartWith) {
        this.titleStartsWith = titleStartWith;
        this.descriptionStartsWith = descriptionStartWith;
    }

    public String getTitleStartsWith() {
        return titleStartsWith;
    }

    public void setTitleStartsWith(String titleStartsWith) {
        this.titleStartsWith = titleStartsWith;
    }

    public String getDescriptionStartsWith() {
        return descriptionStartsWith;
    }

    public void setDescriptionStartsWith(String descriptionStartsWith) {
        this.descriptionStartsWith = descriptionStartsWith;
    }

    @Override
    public String toString() {
        return "GetAllIdeaWithParamDtoRequest{" +
                ", titleStartsWith='" + titleStartsWith + '\'' +
                ", descriptionStartsWith='" + descriptionStartsWith + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetAllIdeasParamRequest request)) return false;
        return Objects.equals(getTitleStartsWith(), request.getTitleStartsWith()) && Objects.equals(getDescriptionStartsWith(), request.getDescriptionStartsWith());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitleStartsWith(), getDescriptionStartsWith());
    }
}
