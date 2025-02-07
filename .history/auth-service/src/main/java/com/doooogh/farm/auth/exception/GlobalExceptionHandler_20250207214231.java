package com.doooogh.farm.auth.exception;

import com.doooogh.farm.common.exception.AuthException;
import com.doooogh.farm.common.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleAuthException(AuthException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBadCredentialsException(BadCredentialsException e) {
        return Result.error(401001, "用户名或密码错误");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return Result.error(401002, "用户不存在");
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleLockedException(LockedException e) {
        return Result.error(401003, "账户已被锁定");
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleDisabledException(DisabledException e) {
        return Result.error(401004, "账户已被禁用");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(403001, "无权访问该资源");
    }
} 