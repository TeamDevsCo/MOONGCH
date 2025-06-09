package com.devsco.moongch.infrastructure;

import com.devsco.moongch.domain.Moongch;
import com.devsco.moongch.domain.MoongchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MoongchRepositoryIpml implements MoongchRepository {

  private final MoongchJpaRepository moongchJpaRepository;

  @Override
  @Transactional
  public Moongch save(Moongch domain) {
    MoongchEntity ent = MoongchEntity.of(domain);
    MoongchEntity saved = moongchJpaRepository.save(ent);
    return saved.toDomain();
  }
}



