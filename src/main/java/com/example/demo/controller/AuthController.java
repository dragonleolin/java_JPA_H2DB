package com.example.demo.controller;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.TokenDTO;
import com.example.demo.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginDTO) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return userAuthenticationService.login(loginDTO.getUserCode(), loginDTO.getPcode());
    }

    @PostMapping("/logout")
    public void logout(@RequestBody TokenDTO tokenDTO) {
        userAuthenticationService.logout(tokenDTO.getAccessToken());
    }
}
