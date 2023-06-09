package ru.smartup.talksscanner.dto.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UpdateRateDtoRequest {
    @NotNull
    @Min(1)
    @Max(5)
    private int rate;

    public UpdateRateDtoRequest(Integer rate) {
        this.rate = rate;
    }

    public UpdateRateDtoRequest() {
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
        UpdateRateDtoRequest that = (UpdateRateDtoRequest) o;
        return Objects.equals(getRate(), that.getRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRate());
    }
}
