package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.service.UserService;
import com.example.demo.validator.annotation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.demo.constant.VerifyRegexConst.PATTERN_USER_CODE;
import static com.example.demo.constant.VerifyRegexConst.PATTERN_USER_SEQ;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping(path = "/{userCode}")
    public UserDTO getUser(@Validator(pattern = PATTERN_USER_CODE) @PathVariable("userCode") String userCode) {
        return userService.getUserByUserCode(userCode);
    }

    @PostMapping
    public UserDTO addUser(@RequestBody UserDTO user) {
        return userService.addUser(user);
    }

    @PutMapping(path = "/{userSeq}")
    public UserDTO editUser(@Validator(pattern = PATTERN_USER_SEQ) @PathVariable("userSeq") String userSeq, @RequestBody UserUpdateDTO user) {
        return userService.editUserByUserSeq(userSeq, user);
    }

    @DeleteMapping(path = "/{userCode}")
    public void removeUser(@Validator(pattern = PATTERN_USER_CODE) @PathVariable("userCode") String userCode) {
        userService.removeUserByUserCode(userCode);
    }
}
