package com.devsco.moongch.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.List;

@Builder
public record CustomErrorResponse(
  String title,
  String detail,
  int status,
  @JsonProperty("invalids")
  List<FieldErrorInfo> invalids
) {


  private CustomErrorResponse(ProblemDetail problem) {
    this(problem.getTitle(), problem.getDetail(), problem.getStatus(), new ArrayList<>());
  }

  public static CustomErrorResponse from(ProblemDetail detail) {
    return new CustomErrorResponse(detail);
  }

  public static CustomErrorResponse withInvalids(MethodArgumentNotValidException ex) {
    CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getBody());
    errorResponse.invalids.addAll(FieldErrorInfo.from(ex.getAllErrors()));
    return errorResponse;
  }

  public static CustomErrorResponse withInvalids(HandlerMethodValidationException ex) {
    CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getBody());
    errorResponse.invalids.addAll(FieldErrorInfo.from(ex.getAllErrors()));
    return errorResponse;
  }

  @Builder
  public record FieldErrorInfo(
    @JsonProperty("field") String field,
    @JsonProperty("message") String message,
    @JsonProperty("arguments") Object[] arguments,
    @JsonProperty("rejectValue") String rejectValue,
    @JsonProperty("codes") String[] codes
  ) {

    public static List<FieldErrorInfo> from(List<? extends MessageSourceResolvable> errors) {
      return errors.stream().map(FieldErrorInfo::from).toList();
    }

    private static FieldErrorInfo from(MessageSourceResolvable error) {
      var builder = FieldErrorInfo.builder();
      if (error instanceof FieldError fieldError) {
        builder.field(fieldError.getField());
      }
      return builder
        .codes(error.getCodes())
        .message(error.getDefaultMessage())
        .arguments(error.getArguments())
        .build();
    }
  }
}
