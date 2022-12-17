package com.task.danskebank;

import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Component("ibanValidator")
public class IbanValidator implements IIbanValidator {
    // Country codes and their corresponding IBAN lengths, according to wikipedia
    // https://en.wikipedia.org/wiki/International_Bank_Account_Number#Validating_the_IBAN
    final static Map<String, Integer> IBAN_LENGTH_BY_COUNTRY = Map.<String, Integer>ofEntries(
            entry("AD", 24), entry("AE", 23), entry("AL", 28), entry("AO", 25), entry("AT", 20), entry("AZ", 28),
            entry("BA", 20), entry("BE", 16), entry("BF", 28), entry("BG", 22), entry("BH", 22), entry("BI", 16), entry("BJ", 28), entry("BR", 29), entry("BY", 28),
            entry("CF", 27), entry("CG", 27), entry("CH", 21), entry("CI", 28), entry("CM", 27), entry("CR", 22), entry("CV", 25), entry("CY", 28), entry("CZ", 24),
            entry("DE", 22), entry("DJ", 27), entry("DK", 18), entry("DO", 28), entry("DZ", 26),
            entry("EE", 20), entry("EG", 29), entry("ES", 24),
            entry("FI", 18), entry("FO", 18), entry("FR", 27),
            entry("GA", 27), entry("GB", 22), entry("GE", 22), entry("GI", 23), entry("GL", 18), entry("GQ", 27), entry("GR", 27), entry("GT", 28), entry("GW", 25),
            entry("HN", 28), entry("HR", 21), entry("HU", 28),
            entry("IE", 22), entry("IL", 23), entry("IQ", 23), entry("IR", 26), entry("IS", 26), entry("IT", 27),
            entry("JO", 30),
            entry("KM", 27), entry("KW", 30), entry("KZ", 20),
            entry("LB", 28), entry("LC", 32), entry("LI", 21), entry("LT", 20), entry("LU", 20), entry("LV", 21), entry("LY", 25),
            entry("MA", 28), entry("MC", 27), entry("MD", 24), entry("ME", 22), entry("MG", 27), entry("MK", 19), entry("ML", 28), entry("MR", 27), entry("MT", 31), entry("MU", 30), entry("MZ", 25),
            entry("NE", 28), entry("NI", 32), entry("NL", 18), entry("NO", 15),
            entry("PK", 24), entry("PL", 28), entry("PS", 29), entry("PT", 25),
            entry("QA", 29),
            entry("RO", 24), entry("RS", 22), entry("RU", 29),
            entry("SA", 24), entry("SC", 31), entry("SD", 18), entry("SE", 24), entry("SI", 19), entry("SK", 24), entry("SM", 27), entry("SN", 28), entry("ST", 25), entry("SV", 28),
            entry("TD", 27), entry("TG", 28), entry("TL", 23), entry("TN", 24), entry("TR", 26),
            entry("UA", 29),
            entry("VA", 22), entry("VG", 24),
            entry("XK", 20)
    );

    // iban contains 2 chars for CountryCode, 2 chars for CheckDigits, and at least 1 for BBAN
    final int MIN_IBAN_LENGTH = 5;

    public boolean isValid(String iban) {
        String trimmedIban = iban.replaceAll("\\s", "");
        String upperIban = trimmedIban.toUpperCase();

        // 1. Check that the total IBAN length is correct as per the country. If not, the IBAN is invalid
        if (upperIban.length() < MIN_IBAN_LENGTH) {
            return false;
        }

        String countryCode = upperIban.substring(0, 2);

        if (!IBAN_LENGTH_BY_COUNTRY.containsKey(countryCode)) {
            return false;
        }

        if (upperIban.length() != IBAN_LENGTH_BY_COUNTRY.get(countryCode)) {
            return false;
        }

        // 2. Move the four initial characters to the end of the string
        String reformattedIban = upperIban.substring(4) + upperIban.substring(0, 4);

        // 3. Replace each letter in the string with two digits, where A = 10, B = 11, ..., Z = 35
        StringBuilder integerIban = new StringBuilder();
        for (char character : reformattedIban.toCharArray()) {
            int charValue = Character.getNumericValue(character);

            if (charValue < 0 || charValue > 35) {
                return false;
            }

            integerIban.append(charValue);
        }

        // 4. Interpret the string as a decimal integer and compute the remainder of that number on division by 97
        // May not fit into long type, so doing modular addition
        return mod97(integerIban.toString()) == 1;
    }

    /**
     * Interprets the string as a decimal integer and computes the remainder of that number on division by 97
     *
     * @param decimalStr a string representing decimal integer
     * @return remainder of the number
     */
    private int mod97(String decimalStr) {
        final int IBAN_MODULUS = 97;
        int res = 0;
        for (char c : decimalStr.toCharArray()) {
            res = (res * 10 + c - '0') % IBAN_MODULUS;
        }
        return res;
    }
}