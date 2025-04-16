package com.devsco.moongch.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateMoongchUseCase {

  @Transactional
  public Long create(CreateMoongchCommand command) {
    return null;
  }
}
