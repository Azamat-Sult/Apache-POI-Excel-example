package com.example.ApachePOIExcelExample.aop;

import com.example.ApachePOIExcelExample.enums.LogMessage;
import com.example.ApachePOIExcelExample.enums.LogType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class AuditLogs {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void getControllers() {}

    @Pointcut("execution(public * *(..))")
    public void getPublicMethods() {}

    @Pointcut("getControllers() && getPublicMethods()")
    public void getEndpoints() {}

    @Before("getEndpoints()")
    public void beforeGetEndpoints(JoinPoint joinPoint) {
        var args = Arrays.asList(joinPoint.getArgs());
        var checkArraysNull = args.stream().allMatch(Objects::nonNull);
        var strArgs = (checkArraysNull) ? args.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")) : "";
        if (strArgs.length() > 512) {
            strArgs = strArgs.substring(0, 512).concat(" ...");
        }
        log.info(String.format(LogMessage.CONTROLLER_CALL.getMessage(), LogType.EXEC,
                joinPoint.getSignature().toShortString(), strArgs));
    }

}