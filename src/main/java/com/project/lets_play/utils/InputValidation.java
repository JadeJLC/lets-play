package com.project.lets_play.utils;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;;

@Component
public class InputValidation {

    public boolean isValidText(String input, boolean allowEmpty) {
    if (input == null) {
        return false;
    }
    if (input.isEmpty()) {
        return allowEmpty;
    }
    return Pattern.matches("^[^${}\\[\\]\"'\\\\]*$", input);
    }

    public boolean areAllValid(boolean allowEmpty, String... inputs) {
    for (String input : inputs) {
        if (!isValidText(input, allowEmpty)) {
            return false;
        }
    }
    return true;
    }

    public boolean isValidNumber(Double n) {
        return n != null && n >= 0;
    }
    
}
