package com.example.demo.dto;

import com.example.demo.bo.UserBO;
import com.example.demo.po.UserPO;
import com.example.demo.validator.annotation.Validator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.BeanUtils;

import static com.example.demo.constant.VerifyRegexConst.PATTERN_PCODE;
import static com.example.demo.constant.VerifyRegexConst.PATTERN_ROLE;
import static com.example.demo.constant.VerifyRegexConst.PATTERN_USER_CODE;

/**
 * Data Transfer Object
 *
 * 數據傳輸物件主要用於遠程調用等需要大量傳輸物件的地方。
 * 比如我們一張表有100個欄位，那麼對應的 PO 就有100個屬性。
 * 但是我們畫面上只要顯示其中的10個欄位，客戶端用 WEB service 來獲取數據，沒有必要把整個 PO 物件傳遞到客戶端，
 * 這時我們就可以用只有這10個屬性的 DTO 來傳遞結果到客戶端，這樣也不會暴露服務端表結構。
 */
public class UserDTO {

    @Validator(pattern = PATTERN_USER_CODE)
    private String userCode;
    @Validator(pattern = PATTERN_PCODE)
    private String pcode;
    @Validator(pattern = PATTERN_ROLE)
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
