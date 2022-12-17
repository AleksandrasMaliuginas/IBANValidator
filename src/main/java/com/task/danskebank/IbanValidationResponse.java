package com.task.danskebank;

public class IbanValidationResponse {
    private final String iban;
    private final boolean isValid;

    public IbanValidationResponse(String iban, boolean isValid) {
        this.iban = iban;
        this.isValid = isValid;
    }

    public String getIban() {
        return iban;
    }

    public boolean getIsValid() {
        return isValid;
    }
}