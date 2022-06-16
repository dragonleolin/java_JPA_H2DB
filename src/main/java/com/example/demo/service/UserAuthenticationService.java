package com.example.demo.service;

import com.example.demo.dao.LogoutTokenDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.TokenDTO;
import com.example.demo.error.DemoException;
import com.example.demo.po.LogoutTokenPO;
import com.example.demo.po.UserPO;
import com.example.demo.util.CryptoUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;

import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0001;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0006;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E9997;
import static com.example.demo.constant.PropertiesConst.PropertiesKeyEL.RSA_PRIVATE_KEY;

@Service
public class UserAuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationService.class);

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LogoutTokenDao logoutTokenDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value(RSA_PRIVATE_KEY)
    private String rsaPrivateKey;

    public TokenDTO login(String username, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String decryptedPcode;
        try {
            decryptedPcode = CryptoUtil.rsaDecryptByPrivateKey(password, rsaPrivateKey);
        } catch (Exception e) {
            LOGGER.error("Decrypted failed by value: " + password, e);
            throw DemoException.createByCode(E9997);
        }

        UserPO userPO = userDao.findByUserCode(username)
                .orElseThrow(() -> DemoException.createByCode(E0001));

        if (!passwordEncoder.matches(decryptedPcode, userPO.getPcode())) {
            throw DemoException.createByCode(E0006);
        }

        return TokenDTO.createByToken(jwtService.generateJWT(userPO));
    }

    public boolean isLogout(String token) {
        return logoutTokenDao.existsById(token);
    }

    @Transactional
    public void logout(String token) {
        try {
            Claims claims = jwtService.parseJwtToken(token);
            Timestamp exp = new Timestamp(claims.getExpiration().getTime());
            LogoutTokenPO logoutTokenPO = new LogoutTokenPO(token, exp);

            logoutTokenDao.save(logoutTokenPO);
        }catch (DemoException e) {
            LOGGER.info("The token you want to logout：{}", token);
            LOGGER.info("The token you want to logout has expired", e);
        }
    }
    
}
