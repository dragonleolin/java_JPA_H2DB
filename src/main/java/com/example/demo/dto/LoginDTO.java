package com.example.demo.dto;

import com.example.demo.validator.annotation.Validator;

import static com.example.demo.constant.VerifyRegexConst.PATTERN_PCODE;
import static com.example.demo.constant.VerifyRegexConst.PATTERN_USER_CODE;

public class LoginDTO {

    @Validator(pattern = PATTERN_USER_CODE)
    private String userCode;
    @Validator(pattern = PATTERN_PCODE)
    private String pcode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }
}
