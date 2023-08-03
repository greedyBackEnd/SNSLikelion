package com.example.likelionSNS.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.stereotype.Controller *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    @Around("springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        long start = System.currentTimeMillis();

        try {
            logger.info("{} - starting~~~ ", joinPoint.getTarget().getClass().getSimpleName());
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            logger.info("Request: {} {}: {} ({}ms)", request.getMethod(), request.getRequestURL(), paramMapToString(request.getParameterMap()), end - start);
        }
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s : %s", entry.getKey(), Arrays.toString(entry.getValue())))
                .collect(Collectors.joining(", "));
    }
}
