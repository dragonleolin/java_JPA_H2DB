package com.example.demo.constant;

import java.util.function.Function;

public enum ValidatorEnum {
    NONE(null),
    VALIDATE_JWT(jwt -> {
        int firstPeriod = jwt.indexOf('.');
        int lastPeriod = jwt.lastIndexOf('.');

        if (firstPeriod <= 0 || lastPeriod <= firstPeriod) {
            return false;
        }
        return true;
    })
    ;

    private Function<String, Boolean> validator;

    ValidatorEnum(Function<String, Boolean> validator) {
        this.validator = validator;
    }

    public Function<String, Boolean> getValidator() {
        return validator;
    }
}
