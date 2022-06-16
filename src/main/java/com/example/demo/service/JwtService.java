package com.example.demo.service;

import com.example.demo.bo.UserBO;
import com.example.demo.dto.UserDTO;
import com.example.demo.error.DemoException;
import com.example.demo.po.UserPO;
import com.example.demo.util.DateTimeUtil;
import com.example.demo.util.ECDSAUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0003;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0005;
import static com.example.demo.constant.PropertiesConst.PropertiesKeyEL.ECDSA_PRIVATE_KEY;
import static com.example.demo.constant.PropertiesConst.PropertiesKeyEL.ECDSA_PUBLIC_KEY;
import static com.example.demo.constant.PropertiesConst.PropertiesKeyEL.JWT_EXPIRATION_MIN;
import static com.example.demo.util.DateTimeUtil.isTimeBeforeCurrent;

@Service
public class JwtService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private UserService userService;
    @Value(JWT_EXPIRATION_MIN)
    private int jwtExpMin;
    @Value(ECDSA_PRIVATE_KEY)
    private String ecdsaPrivateKey;
    @Value(ECDSA_PUBLIC_KEY)
    private String ecdsaPublicKey;

    public String generateJWT(UserPO user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        List<String> authorities = new ArrayList<>();
        authorities.add(user.getRole());

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(user.getUserSeq()))
                .setExpiration(generateAccessTokenExpiration())
                .signWith(SignatureAlgorithm.ES256, ECDSAUtil.transferToECPrivateKey(ecdsaPrivateKey))
                .compact();
    }

    public UserBO validateJWT(String token) {
        Claims body = parseJwtToken(token, ecdsaPublicKey);

        if (isTimeBeforeCurrent(body.getExpiration())) {
            throw DemoException.createByCode(E0003);
        }

        UserDTO userDTO = userService.getUserByUserSeq(Integer.parseInt(body.getSubject()));

        return userDTO.getUserBO();
    }

    public Claims parseJwtToken(String token) {
        return parseJwtToken(token, ecdsaPublicKey);
    }

    private Claims parseJwtToken(String token, String key) {
        try {
            return Jwts.parser()
                    .setSigningKey(ECDSAUtil.transferToECPublicKey(key))
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            LOGGER.error("JWT parser error：" + token, e);
            throw DemoException.createByCode(E0005);
        }
    }

    private Timestamp generateAccessTokenExpiration() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateTimeUtil.getCurrentTime());
        calendar.add(Calendar.MINUTE, jwtExpMin);
        return new Timestamp(calendar.getTimeInMillis());
    }
}
