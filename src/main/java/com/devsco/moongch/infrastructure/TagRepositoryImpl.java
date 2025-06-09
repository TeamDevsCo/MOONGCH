package com.devsco.moongch.infrastructure;

import com.devsco.moongch.domain.Tag;
import com.devsco.moongch.domain.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

  private final TagJpaRepository jpa;

  @Override
  public Optional<Tag> findByName(String name) {
    return jpa.findByName(name).map(TagEntity::toDomain);
  }

  @Override
  public Tag save(Tag tag) {
    TagEntity ent = TagEntity.of(tag);
    TagEntity saved = jpa.save(ent);
    return saved.toDomain();
  }
}
