package com.task.danskebank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/iban")
public class IbanController {
    private final IIbanValidator ibanValidator;

    @Autowired
    public IbanController(IIbanValidator ibanValidator) {
        this.ibanValidator = ibanValidator;
    }

    @GetMapping("/{iban}")
    public IbanValidationResponse validateIban(@PathVariable String iban) {
        return new IbanValidationResponse(iban, ibanValidator.isValid(iban));
    }
}