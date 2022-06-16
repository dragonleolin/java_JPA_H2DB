package com.example.demo.validator.annotation;

import com.example.demo.constant.ValidatorEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.example.demo.constant.DemoResponseConst.ReturnCode;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E9998;
import static com.example.demo.constant.ValidatorEnum.NONE;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface Validator {

    /**
     * 定義正則表示式來檢核資料格式
     */
    String pattern() default "";
    /**
     * 定義檢核錯誤時回傳的錯誤代碼
     */
    ReturnCode returnCode() default E9998;
    /**
     * 是否允許null
     */
    boolean notNull() default true;
    /**
     * 定義複雜的檢核邏輯
     */
    ValidatorEnum validator() default NONE;
    /**
     * 若欄位為集合，或多層物件，則設true
     */
    boolean recursive() default false;
}
