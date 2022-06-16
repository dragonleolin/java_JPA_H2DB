package com.example.demo.dto;

import com.example.demo.validator.annotation.Validator;

import static com.example.demo.constant.ValidatorEnum.VALIDATE_JWT;

public class TokenDTO {

    @Validator(validator = VALIDATE_JWT)
    private String accessToken;

    public static TokenDTO createByToken(String token) {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(token);
        return tokenDTO;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
