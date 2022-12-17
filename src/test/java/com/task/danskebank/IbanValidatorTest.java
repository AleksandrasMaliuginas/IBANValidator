package com.task.danskebank;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class IbanValidatorTest {

    @Autowired
    IIbanValidator ibanValidator;

    @ParameterizedTest
    @ValueSource(strings = {
            "GB82 WEST 1234 5698 7654 32  ",
            "  nl78abna2490067532",
            "VG62WTQT5792371874445196"
    })
    void isValid(String iban) {
        assertTrue(ibanValidator.isValid(iban));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "GB82 WEST 1234 <598 7654 32  ",
            "nl78abna24900675321010",
            "xx92359688684256",
            "LT 75",
            ""
    })
    void isInvalid(String iban) {
        assertFalse(ibanValidator.isValid(iban));
    }
}