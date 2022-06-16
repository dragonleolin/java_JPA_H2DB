package com.example.demo.validator.request;

import com.example.demo.constant.ValidatorEnum;
import com.example.demo.error.DemoException;
import com.example.demo.util.PojoUtil;
import com.example.demo.validator.annotation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import static com.example.demo.constant.DemoResponseConst.ReturnCode;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E9998;
import static com.example.demo.constant.ValidatorEnum.NONE;

@Component
public class RequestValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestValidator.class);

    public void validateParameter(Validator annotation, Object fieldValue, Parameter parameter) {
        this.checkNotNull(annotation, fieldValue, parameter.getName());
        this.checkPattern(annotation, fieldValue, parameter.getName(), parameter.getType().getName());
        this.checkValidateLogic(annotation, fieldValue, parameter.getName());
    }

    public void validate(Object inputObj) {

        Field[] declaredFields = inputObj.getClass().getDeclaredFields();

        // key is method name in lower case
        Map<String, Method> methodMap = PojoUtil.getMethodsOfObj(inputObj);

        for (Field field : declaredFields) {

            String fieldName = field.getName();
            String methodName = "get" + fieldName.toLowerCase();
            Method method = methodMap.get(methodName);

            // no getter, don't validate
            if (method == null) {
                continue;
            }

            // get @ValidateAll/ValidateCCDTM/ValidateCCDTW
            Validator annotation = this.getValidateAnnotation(field);

            Object fieldValue = PojoUtil.invokeMethod(method, inputObj);

            // no @ValidateAll/ValidateCCDTM/ValidateCCDTW, don't test
            if (annotation == null) {
                continue;
            }

            // check field value according to annotation
            this.checkNotNull(annotation, fieldValue, field.getName());
            this.checkPattern(annotation, fieldValue, field.getName(), field.getType().getName());
            this.checkValidateLogic(annotation, fieldValue, field.getName());
            this.checkRecursive(annotation, fieldValue, field);

        }

    }

    private Validator getValidateAnnotation(Field field) {

        Validator result = field.getAnnotation(Validator.class);

        return result;

    }

    private void checkNotNull(Validator annotation, Object fieldValue, String fieldName) {

        ReturnCode returnCode = annotation.returnCode();
        boolean isNotNull = annotation.notNull();

        if (isNotNull) {

            if (fieldValue == null) {
                throw DemoException.createByCodeAndExtInfo(returnCode,
                        String.format("Field: [%s] should not be null", fieldName));

            }

        }

    }

    private void checkPattern(Validator annotation, Object fieldValue, String fieldName, String fieldTypeName) {

        ReturnCode returnCode = annotation.returnCode();
        String pattern = annotation.pattern();

        if (fieldValue == null) {
            return;
        }

        if (StringUtils.isNotBlank(pattern)) {

            if (!(fieldValue instanceof String)) {
                throw DemoException.createByCodeAndExtInfo(returnCode,
                        String.format("pattern should not be used at field with type %s", fieldTypeName));
            }

            if (!((String) fieldValue).matches(pattern)) {
                if (E9998 == returnCode) {
                    throw DemoException.createByCodeAndExtInfo(returnCode,
                            String.format("Field: [%s] with value [%s] not match regular expression \"%s\"",
                                    fieldName, fieldValue, pattern));
                } else {
                    throw DemoException.createByCode(returnCode);
                }
            }

        }

    }

    private void checkValidateLogic(Validator annotation, Object fieldValue, String fieldName) {

        ReturnCode returnCode = annotation.returnCode();
        ValidatorEnum validator = annotation.validator();

        if (fieldValue == null) {
            return;
        }

        if (!NONE.equals(validator)) {

            if (!validator.getValidator().apply((String)fieldValue)) {
                throw DemoException.createByCodeAndExtInfo(returnCode,
                        String.format("Field: [%s] does not match the check logic", fieldName));
            }
        }
    }

    private void checkRecursive(Validator annotation, Object fieldValue, Field field) {

        boolean isRecursive = annotation.recursive();

        if (fieldValue == null) {
            return;
        }

        if (isRecursive) {

            if (Iterable.class.isAssignableFrom(field.getType())) {
                for (Object iteratedObj : (Iterable) fieldValue) {
                    this.validate(iteratedObj);
                }
            } else {
                this.validate(fieldValue);
            }

        }

    }
}
