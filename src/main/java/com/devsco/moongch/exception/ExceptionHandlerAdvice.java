package com.devsco.moongch.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(BAD_REQUEST)
  public CustomErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    return CustomErrorResponse.withInvalids(ex);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  @ResponseStatus(BAD_REQUEST)
  public CustomErrorResponse handleHandlerMethodArgumentNotValidException(HandlerMethodValidationException ex) {
    return CustomErrorResponse.withInvalids(ex);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public CustomErrorResponse handleNoResourceFoundException(NoResourceFoundException ex) {
    return CustomErrorResponse.from(ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage()));
  }

  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
  @ResponseStatus(BAD_REQUEST)
  public CustomErrorResponse handleBadRequestTypeException(RuntimeException ex) {
    return CustomErrorResponse.from(ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public CustomErrorResponse handleRestException(Exception ex) {
    log.error(INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
    return CustomErrorResponse.from(ProblemDetail.forStatus(INTERNAL_SERVER_ERROR));
  }
}
