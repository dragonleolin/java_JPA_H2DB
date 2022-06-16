package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.error.DemoException;
import com.example.demo.po.UserPO;
import com.example.demo.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static com.example.demo.constant.CacheConst.CACHE_NAME_USER_CACHE;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0001;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E9997;
import static com.example.demo.constant.PropertiesConst.PropertiesKeyEL.RSA_PRIVATE_KEY;
import static java.util.stream.Collectors.toList;

@Service
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value(RSA_PRIVATE_KEY)
    private String rsaPrivateKey;

    public UserDTO getUserByUserSeq(int userSeq) {
        return userDao.findById(userSeq)
                .map(UserDTO::createByUserPO)
                .orElseThrow(() -> DemoException.createByCode(E0001));
    }

    @Cacheable(cacheNames = CACHE_NAME_USER_CACHE, key = "#root.methodName")
    public List<UserDTO> getAllUser() {
        return StreamSupport.stream(userDao.findAll().spliterator(), false)
                .map(UserDTO::createByUserPO)
                .collect(toList());
    }

    @Cacheable(cacheNames = CACHE_NAME_USER_CACHE, key = "#userCode")
    public UserDTO getUserByUserCode(String userCode) {
        return userDao.findByUserCode(userCode)
                .map(UserDTO::createByUserPO)
                .orElseThrow(() -> DemoException.createByCode(E0001));
    }

    @Transactional
    @CachePut(cacheNames = CACHE_NAME_USER_CACHE, key = "#userDTO.userCode")
    public UserDTO addUser(UserDTO userDTO) {
        userDTO.setPcode(processPcode(userDTO.getPcode()));
        UserPO userPO = userDTO.getUserPO();

        return UserDTO.createByUserPO(userDao.save(userPO));
    }

    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME_USER_CACHE, key = "#userCode")
    public void removeUserByUserCode(String userCode) {
        userDao.deleteByUserCode(userCode);
    }

    @Transactional
    @CachePut(cacheNames = CACHE_NAME_USER_CACHE, key = "#result.userCode")
    public UserDTO editUserByUserSeq(String userSeq, UserUpdateDTO user) {
        return updateUser(
                dao -> dao.findById(Integer.parseInt(userSeq)).orElseThrow(() -> DemoException.createByCodeAndExtInfo(E0001, "Can't found User by userSeq.")),
                userPO -> {
                    if (StringUtils.hasText(user.getUserCode()))
                        userPO.setUserCode(user.getUserCode());
                    if (StringUtils.hasText(user.getPcode()))
                        userPO.setPcode(processPcode(user.getPcode()));
                    if (StringUtils.hasText(user.getRole()))
                        userPO.setRole(user.getRole());
                }
        );
    }

    private UserDTO updateUser(Function<UserDao, UserPO> selector, Consumer<UserPO> setter) {
        UserPO user = selector.apply(userDao);
        setter.accept(user);

        return UserDTO.createByUserPO(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = getUserByUserCode(username);
        return userDTO.getUserBO();
    }

    private String processPcode(String inputPcode) {
        String decryptedPcode;
        try {
            decryptedPcode = CryptoUtil.rsaDecryptByPrivateKey(inputPcode, rsaPrivateKey);
        } catch (Exception e) {
            LOGGER.error("Decrypted failed by value: ", e);
            throw DemoException.createByCode(E9997);
        }

        return passwordEncoder.encode(decryptedPcode);
    }
}
