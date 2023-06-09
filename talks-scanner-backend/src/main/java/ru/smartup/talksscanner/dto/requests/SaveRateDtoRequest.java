package ru.smartup.talksscanner.dto.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SaveRateDtoRequest {
    @NotNull
    @Min(1)
    @Max(5)
    private int rate;

    public SaveRateDtoRequest(int rate) {
        this.rate = rate;
    }

    public SaveRateDtoRequest() {
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
        SaveRateDtoRequest that = (SaveRateDtoRequest) o;
        return Objects.equals(getRate(), that.getRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRate());
    }
}
