package com.devsco.moongch.application;

import com.devsco.moongch.domain.Moongch;
import com.devsco.moongch.domain.Snippets;
import com.devsco.moongch.domain.SnippetsLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateMoongchUseCase {

  @Transactional
  public Long create(CreateMoongchCommand command) {
    Moongch moongch = Moongch.builder()
      .title(command.title())
      .description(command.description())
      .build();

    List<Snippets> snippets = command.snippets().stream().map(s -> {
      Snippets snippet = new Snippets(s.ext());
        Snippets.builder().ext(s.ext()).build();
      s.lines().forEach(l -> {
        SnippetsLine line = SnippetsLine.builder().lineNo(l.lineNo()).text(l.text()).build();
        snippet.getSnippetsLines().add(line);
      });
      return snippet;
    }).toList();
    moongch.getSnippets().addAll(snippets);

    return null;
  }
}
