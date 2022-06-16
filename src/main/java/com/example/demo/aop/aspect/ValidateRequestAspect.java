package com.example.demo.aop.aspect;

import com.example.demo.validator.annotation.Validator;
import com.example.demo.validator.request.RequestValidator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.demo.constant.AOPConst.POINTCUT_WEBLAYER;
import static com.example.demo.constant.AOPConst.VALIDATE_REQUEST_ORDER;

@Component
@Aspect
@Order(VALIDATE_REQUEST_ORDER)
public class ValidateRequestAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateRequestAspect.class);

    @Autowired
    private RequestValidator requestValidator;

    @Before(value = POINTCUT_WEBLAYER)
    public void validateRequest(JoinPoint joinPoint) {
        Stream<Method> methodStream = Arrays.stream(joinPoint.getTarget().getClass().getMethods());

        Optional<Method> invokedMethodOpt = methodStream
                .filter(method -> method.getName().equals(joinPoint.getSignature().getName()))
                .findAny();

        Object[] args = joinPoint.getArgs();

        invokedMethodOpt.ifPresent(method -> {
            Parameter[] parameters = method.getParameters();

            IntStream.range(0, parameters.length)
                    .forEach(i -> {
                        boolean isBodyParameter =
                                Optional.ofNullable(parameters[i].getAnnotation(RequestBody.class)).isPresent();

                        if (isBodyParameter) {
                            Object arg = args[i];
                            requestValidator.validate(arg);
                        } else {
                            Optional<Validator> validatorOpt =
                                    Optional.ofNullable(parameters[i].getAnnotation(Validator.class));

                            validatorOpt.ifPresent(validator ->
                                    requestValidator.validateParameter(validator, args[i], parameters[i]));
                        }
                    });
        });
    }
}
