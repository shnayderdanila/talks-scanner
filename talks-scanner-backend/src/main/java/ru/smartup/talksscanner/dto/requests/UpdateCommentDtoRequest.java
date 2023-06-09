package ru.smartup.talksscanner.dto.requests;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class UpdateCommentDtoRequest {
    @NotBlank
    private String text;

    public UpdateCommentDtoRequest(String text) {
        this.text = text;
    }

    public UpdateCommentDtoRequest() {
    }

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
        UpdateCommentDtoRequest that = (UpdateCommentDtoRequest) o;
        return Objects.equals(getText(), that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText());
    }
}
