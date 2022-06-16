package com.example.demo.aop.poincut;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointcutDefinition {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void webLayer() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceLayer() {
    }
}
