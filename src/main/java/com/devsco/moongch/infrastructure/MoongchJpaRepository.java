package com.devsco.moongch.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MoongchJpaRepository extends JpaRepository<MoongchEntity, Long> {
}
