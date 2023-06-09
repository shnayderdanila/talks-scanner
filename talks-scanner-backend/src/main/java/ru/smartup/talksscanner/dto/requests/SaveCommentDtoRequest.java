package ru.smartup.talksscanner.dto.requests;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * DTO request for new comments.
 */
public class SaveCommentDtoRequest {

    @NotBlank
    private String text;

    public SaveCommentDtoRequest(String text) {
        this.text = text;
    }

    public SaveCommentDtoRequest() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveCommentDtoRequest that = (SaveCommentDtoRequest) o;
        return Objects.equals(getText(), that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText());
    }
}
