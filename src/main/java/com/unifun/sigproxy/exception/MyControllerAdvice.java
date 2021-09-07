package com.unifun.sigproxy.exception;

import com.unifun.sigproxy.controller.dto.ApiBaseErrorDto;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
@Log4j
@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(SS7NotContentException.class)
    @ResponseBody
    public ApiBaseErrorDto handleNoContentEntity(SS7NotContentException ex, HttpServletRequest request){
        log.info(String.valueOf(ex));
        return ApiBaseErrorDto.builder()
                .patch(request.getRequestURI())
                .code(HttpStatus.NO_CONTENT.value())
                .type(ex.getClass().getName())
                .detail(ex.getMessage())
                .build();
    }

    @ExceptionHandler(SS7NotFoundException.class)
    @ResponseBody
    public ApiBaseErrorDto handleNotFoundEntity(SS7NotFoundException ex, HttpServletRequest request){
        log.info(String.valueOf(ex));
        return ApiBaseErrorDto.builder()
                .patch(request.getRequestURI())
                .code(HttpStatus.NOT_FOUND.value())
                .type(ex.getClass().getName())
                .detail(ex.getMessage())
                .build();
    }

    @ExceptionHandler(SS7AddClientLinkException.class)
    @ResponseBody
    public ApiBaseErrorDto handleAddClientLinkException(SS7AddClientLinkException ex, HttpServletRequest request){
        log.info(String.valueOf(ex));
        return ApiBaseErrorDto.builder()
                .patch(request.getRequestURI())
                .code(HttpStatus.BAD_REQUEST.value())
                .type(ex.getClass().getName())
                .detail(ex.getMessage())
                .build();
    }
}