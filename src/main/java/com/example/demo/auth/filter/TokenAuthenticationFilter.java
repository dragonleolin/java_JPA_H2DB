package com.example.demo.auth.filter;

import com.example.demo.dto.ErrorDTO;
import com.example.demo.error.DemoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0007;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private static final String BEARER = "Bearer";

    public TokenAuthenticationFilter(final RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            final String token = Optional.ofNullable(request.getHeader(AUTHORIZATION))
                    .map(value -> StringUtils.removeStart(value, BEARER))
                    .map(String::trim)
                    .orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));

            final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
            return getAuthenticationManager().authenticate(auth);
        }catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            ErrorDTO errorDTO = new ErrorDTO();

            if (e instanceof DemoException) {
                DemoException de = (DemoException) e;
                errorDTO.setReturnCode(de.getReturnCode());
                errorDTO.setReturnMsg(de.getExtInfo());
                response.setStatus(HttpStatus.FORBIDDEN.value());
            } else {
                errorDTO.setReturnCode(E0007.name());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }

            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response.getWriter().write(convertObjectToJson(errorDTO));
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
