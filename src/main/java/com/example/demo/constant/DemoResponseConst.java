package com.example.demo.constant;

public class DemoResponseConst {

    public enum ReturnCode {
        /** Success */
        S0000,

        /** User not found by userCode */
        E0001,
        /** No provide token */
        E0002,
        /** Token is expired */
        E0003,
        /** JWT algorithms not found */
        E0004,
        /** JWT validate failed */
        E0005,
        /** Login faild */
        E0006,
        /** Authentication faild */
        E0007,
        /** JWT has been logout */
        E0008,

        /** Decrypt fail */
        E9997,
        /** Validate request fields fail */
        E9998,
        /** System error */
        E9999
    }
}
