package com.devsco.moongch.application;

import com.devsco.moongch.domain.Moongch;
import com.devsco.moongch.domain.MoongchRepository;
import com.devsco.moongch.domain.Snippets;
import com.devsco.moongch.domain.SnippetsLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateMoongchUseCase {

  private final MoongchRepository moongchRepository;

  @Transactional
  public Long create(CreateMoongchCmd cmd) {
    Moongch moongch = Moongch.builder()
      .usersId(cmd.userId())
      .title(cmd.title())
      .description(cmd.description())
      .isTemp(cmd.isTemp())
      .isOrigin(cmd.isOrigin())
      .type(Moongch.Type.valueOf(cmd.type().toUpperCase()))
      .originMoongchId(cmd.originMoongchId())
      .tags(cmd.tags())
      .build();

    List<Snippets> snippets = cmd.snippets().stream()
      .map(sDto -> {
        Snippets snippet = Snippets.builder()
          .ext(sDto.ext())
          .build();

        sDto.lines().forEach(lDto -> {
          SnippetsLine line = SnippetsLine.builder()
            .lineNo(lDto.lineNo())
            .text(lDto.text())
            .build();
          snippet.getSnippetsLines().add(line);
        });

        return snippet;
      })
      .toList();

    moongch.getSnippets().addAll(snippets);


    Moongch savedMoongch = moongchRepository.save(moongch);

    return savedMoongch.getId();
  }
}
