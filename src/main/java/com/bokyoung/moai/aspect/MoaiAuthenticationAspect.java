package com.bokyoung.moai.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MoaiAuthenticationAspect {

    @Pointcut("@annotation(auth)")
    public void authPointcut(Auth auth) {
    }

    @Before("authPointcut(auth)")
    public void authenticationCheckBefore(JoinPoint joinPoint, Auth auth) {
        MoaiAuthCheck.checkAuthorities(auth.type(), MoaiAuthCheck.getLoginId());
    }
}

