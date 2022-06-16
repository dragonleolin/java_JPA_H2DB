package com.example.demo.constant;

import com.example.demo.error.DemoException;

import java.util.Arrays;

import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0004;

public enum JwtAlgorithms {

    ES256("ES256", "SHA256withECDSA");

    private String alg;
    private String javaName;

    JwtAlgorithms(String alg, String javaName) {
        this.alg = alg;
        this.javaName = javaName;
    }

    public String getAlg() {
        return alg;
    }

    public String getJavaName() {
        return javaName;
    }

    public static JwtAlgorithms getInstanceByJavaName(String javaName) {
        return Arrays.stream(JwtAlgorithms.values())
                .filter(jwtAlgorithms -> jwtAlgorithms.getJavaName().equals(javaName))
                .findAny()
                .orElseThrow(() -> DemoException.createByCode(E0004));
    }
}
