package com.devsco.moongch.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MockController.class)
@ActiveProfiles("test")
@Slf4j
class ExceptionHandlerAdviceTest {

  @Autowired ObjectMapper objectMapper;
  @Autowired MockMvc mockMvc;

  @DisplayName("HandlerMethodArgumentNotValidException 예외 처리 테스트")
  @Test
  @WithMockUser
  void handleHandlerMethodArgumentNotValidException() throws Exception {
    //given
    MockController.MockRequest request = new MockController.MockRequest(null, 0);

    //when
    ResultActions resultActions = mockMvc.perform(
      post("/mock")
        .with(csrf())
        .content(objectMapper.writeValueAsString(request))
        .contentType("application/json")
    );

    //then
    resultActions
      .andExpect(status().is4xxClientError())
      .andExpect(result -> {
        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        CustomErrorResponse errorResponse =
          objectMapper.readValue(result.getResponse().getContentAsByteArray(), CustomErrorResponse.class);
        assertThat(errorResponse.invalids()).hasSize(1);
        assertThat(errorResponse.invalids())
          .extracting("field", "message")
          .containsExactlyInAnyOrder(
            tuple("name", "must not be null")
          );
      })
      .andDo(print());
  }

  @DisplayName("NoResourceFoundException 예외 처리 테스트")
  @Test
  @WithMockUser
  void handleNoResourceFoundException() throws Exception {
    //given
    MockController.MockRequest request = new MockController.MockRequest("홍길동", 20);

    //when
    ResultActions resultActions = mockMvc.perform(
      post("/make-no-resource-ex")
        .with(csrf())
        .content(objectMapper.writeValueAsString(request))
        .contentType("application/json")
    );

    //then
    resultActions
      .andExpect(status().is4xxClientError())
      .andExpect(result -> {
        assertThat(result.getResolvedException()).isInstanceOf(NoResourceFoundException.class);
        CustomErrorResponse errorResponse =
          objectMapper.readValue(result.getResponse().getContentAsByteArray(), CustomErrorResponse.class);
        assertThat(errorResponse)
          .extracting("title", "detail", "status")
          .contains("Not Found", "No static resource make-no-resource-ex.", 404)
        ;
      })
      .andDo(print());
  }

  @DisplayName("IllegalArgumentException 예외 처리 테스트")
  @Test
  @WithMockUser
  void test() throws Exception {
    //given
    //when
    ResultActions resultActions = mockMvc.perform(
      get("/mock").with(csrf())
    );

    //then
    resultActions
      .andExpect(status().is4xxClientError())
      .andExpect(result -> {
        assertThat(result.getResolvedException()).isInstanceOf(IllegalArgumentException.class);
        CustomErrorResponse errorResponse =
          objectMapper.readValue(result.getResponse().getContentAsByteArray(), CustomErrorResponse.class);
        assertThat(errorResponse)
          .extracting("title", "detail", "status")
          .contains("Bad Request", "IllegalArgumentException 발생", 400)
        ;
      })
      .andDo(print());
  }
}
