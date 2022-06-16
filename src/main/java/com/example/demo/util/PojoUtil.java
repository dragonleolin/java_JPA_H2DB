package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PojoUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PojoUtil.class);

    /**
     * key is the methodName in lowercase
     *
     * @param inputObj
     * @return
     */
    public static Map<String, Method> getMethodsOfObj(Object inputObj) {

        Map<String, Method> methodMap = new HashMap<>();
        MethodDescriptor[] methodDescriptors = new MethodDescriptor[0];

        try {
            methodDescriptors = Introspector.getBeanInfo(inputObj.getClass()).getMethodDescriptors();
        } catch (IntrospectionException e) {
            LOGGER.debug(e.getMessage(), e);
        }

        for (MethodDescriptor methodDescriptor : methodDescriptors) {
            Method m = methodDescriptor.getMethod();
            methodMap.put(m.getName().toLowerCase(), m);
        }

        return methodMap;

    }

    public static Object invokeMethod(Method method, Object obj) {

        try {
            return method.invoke(obj);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOGGER.debug(e.getMessage(), e);
        }

        return null;
    }
}
