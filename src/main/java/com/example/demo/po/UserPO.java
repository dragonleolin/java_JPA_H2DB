package com.example.demo.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistant Object 持久物件
 *
 * 一個 PO 就是 DB 中的一條記錄。好處是可以把一條記錄作為一個物件處理，可以方便的轉為其它物件。
 */
@Entity
@Table(name = "DEMO_USER")
@DynamicInsert
@DynamicUpdate
public class UserPO {

    @Id
    @Column(name = "USER_SEQ", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userSeq;

    @Column(name = "USER_CODE", unique = true, length = 30)
    private String userCode;

    @Column(name = "PCODE", length = 500)
    private String pcode;

    @Column(name = "USER_ROLE", length = 30)
    private String role;

    public int getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
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
