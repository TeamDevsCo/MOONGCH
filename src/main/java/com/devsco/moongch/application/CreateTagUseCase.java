package com.devsco.moongch.application;

import com.devsco.moongch.domain.Tag;
import com.devsco.moongch.domain.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTagUseCase {

  private final TagRepository tagRepository;

  @Transactional
  public Tag createTag(String tagName) {
    return tagRepository.findByName(tagName)
      .orElseGet(() -> tagRepository.save(
        Tag.builder().name(tagName).build()
      ));
  }
}
