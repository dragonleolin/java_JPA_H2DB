package com.example.demo.auth.service;

import com.example.demo.error.DemoException;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0002;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0008;

@Service
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationProvider.class);

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        final Object token = authentication.getCredentials();
        String jwtString = Optional.ofNullable(token)
                .map(String::valueOf)
                .orElseThrow(() -> DemoException.createByCode(E0002));

        if (userAuthenticationService.isLogout(jwtString)) {
            LOGGER.info("Has logout token：{}", jwtString);
            throw DemoException.createByCode(E0008);
        }

        return jwtService.validateJWT(jwtString);
    }
}
