package com.devsco.moongch.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateMoongchUseCaseTest {
  CreateMoongchUseCase createMoongchUseCase;
  @BeforeEach
  void setUp() {
    createMoongchUseCase = new CreateMoongchUseCase();
  }

  @DisplayName("유저는 뭉치를 생성 할 수 있다")
  @Test
  void create(){
    //given
    CreateMoongchCmd command = CreateMoongchCmd.builder()
      .title("제목")
      .description("설명")
      .build();

    //when
    Long moongchId = createMoongchUseCase.create(command);

    //then
    Assertions.assertThat(moongchId).isNotNull();
  }
}
