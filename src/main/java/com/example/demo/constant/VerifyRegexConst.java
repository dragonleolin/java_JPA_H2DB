package com.example.demo.constant;

public class VerifyRegexConst {

    public static final String PATTERN_USER_SEQ = "^\\d+$";
    public static final String PATTERN_USER_CODE = "^\\w{5,30}$";
    public static final String PATTERN_PCODE = "^[A-F0-9]{256}$";
    public static final String PATTERN_ROLE = "^ROLE_[A-Z0-9]{1,30}$";
    //判斷含中文字的寫法 ^[A-z0-9\\u4e00-\\u9fa5]*$
    public static final String PATTERN_BOOK_NAME = "^[A-z0-9\\s]+{255}$";
    public static final String PATTERN_BOOK_AUTHOR = "^[A-Za-z\\s]+{255}$";
    //時間格式的正則2022-06-12 ^\\d{4}-\\d{2}-\\d{2}
    public static final String PATTERN_BOOK_PUBLICATIONDATE = "^\\d+{6}$";
}
