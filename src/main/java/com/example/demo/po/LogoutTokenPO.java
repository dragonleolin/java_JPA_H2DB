package com.example.demo.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "LOGOUT_TOKEN")
public class LogoutTokenPO {

    @Id
    @Column(name = "TOKEN", nullable = false, length = 300)
    private String token;

    @Column(name = "TOKEN_EXP", nullable = false)
    private Timestamp tokenExp;

    public LogoutTokenPO() {}

    public LogoutTokenPO(String token, Timestamp exp) {
        this.token = token;
        this.tokenExp = exp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getTokenExp() {
        return tokenExp;
    }

    public void setTokenExp(Timestamp tokenExp) {
        this.tokenExp = tokenExp;
    }
}
