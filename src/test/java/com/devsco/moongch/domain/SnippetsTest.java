package com.devsco.moongch.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SnippetsTest {

  @DisplayName("")
  @Test
  void create(){
    //given
    String ext = "java";
    SnippetsLines line1 = SnippetsLines.builder().build();
    SnippetsLines line2 = SnippetsLines.builder().build();

    //when
    Snippets snippets = Snippets.create(ext, List.of(line1, line2));

    //then
    assertThat(snippets).hasFieldOrPropertyWithValue("ext", ext);
    assertThat(snippets.getSnippetsLines()).hasSize(2);
    assertThat(snippets.getSnippetsLines()).contains(line1, line2);
  }

  @DisplayName("Snippets 전체 내용을 줄 단위로 합쳐 반환할 수 있다")
  @Test
  void getFullContent() {
    //given
    SnippetsLines line1 = SnippetsLines.builder().text("동해물과").build();
    SnippetsLines line2 = SnippetsLines.builder().text("백두산이").build();
    SnippetsLines line3 = SnippetsLines.builder().text("마르고 닳도록").build();

    Snippets snippets = Snippets.builder().ext("java").build();
    snippets.getSnippetsLines().addAll(List.of(line1, line2, line3));

    //when
    String fullContent = snippets.getFullContent();

    //then
    assertThat(fullContent).isEqualTo("동해물과\n백두산이\n마르고 닳도록");
  }

}
