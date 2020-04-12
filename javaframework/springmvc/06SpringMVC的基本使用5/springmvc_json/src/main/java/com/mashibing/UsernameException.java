package com.mashibing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "名字错误",value = HttpStatus.NOT_ACCEPTABLE)
public class UsernameException extends RuntimeException {
}
