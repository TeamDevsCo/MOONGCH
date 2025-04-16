package com.devsco.moongch.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MoongchTest {
  @DisplayName("뭉치를 생성 할 수 있다")
  @Test
  void create(){
    //given
    String title = "title";
    String description = "description";
    Snippets snippets1 = Snippets.builder().build();
    Snippets snippets2 = Snippets.builder().build();

    //when
    Moongch moongch = Moongch.create(title, description, List.of(snippets1, snippets2));

    //then
    assertThat(moongch).extracting("title", "description").contains(title, description);
    assertThat(moongch.getSnippets()).containsExactly(snippets1, snippets2);
  }
}
