package org.larin.budget.data.dto;

import javax.validation.constraints.NotEmpty;

public class CurrencyIdDTO {
    @NotEmpty
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
