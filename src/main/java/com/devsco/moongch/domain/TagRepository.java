package com.devsco.moongch.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository {

  Optional<Tag> findByName(String name);

  Tag save(Tag build);
}
