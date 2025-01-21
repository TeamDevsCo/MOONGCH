package com.devsco.moongch.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
public class MockController {
  @PostMapping
  public String call(@Valid @RequestBody MockRequest request) {
    return "Hello, " + request.name();
  }

  @GetMapping
  public String call2() throws IllegalArgumentException {
    if (true) {
      throw new IllegalArgumentException("IllegalArgumentException 발생");
    }
    return "Hello";
  }

  record MockRequest(
    @NotNull String name,
    int age) {
  }
}
