package com.example.demo.dto;

import com.example.demo.bo.UserBO;
import com.example.demo.po.UserPO;
import com.example.demo.validator.annotation.Validator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.BeanUtils;

import static com.example.demo.constant.VerifyRegexConst.PATTERN_PCODE;
import static com.example.demo.constant.VerifyRegexConst.PATTERN_ROLE;
import static com.example.demo.constant.VerifyRegexConst.PATTERN_USER_CODE;

public class UserUpdateDTO {

    @Validator(notNull = false, pattern = PATTERN_USER_CODE)
    private String userCode;
    @Validator(notNull = false, pattern = PATTERN_PCODE)
    private String pcode;
    @Validator(notNull = false, pattern = PATTERN_ROLE)
    private String role;

    public static UserDTO createByUserPO(UserPO po) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(po, dto);

        return dto;
    }

    @JsonIgnore
    public UserBO getUserBO() {
        UserBO bo = new UserBO();
        BeanUtils.copyProperties(this, bo);

        return bo;
    }

    @JsonIgnore
    public UserPO getUserPO() {
        UserPO po = new UserPO();
        BeanUtils.copyProperties(this, po);

        return po;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
