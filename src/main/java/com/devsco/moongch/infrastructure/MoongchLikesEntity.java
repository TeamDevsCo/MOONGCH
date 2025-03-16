package com.devsco.moongch.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "moongch", name = "moongch_likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access =AccessLevel.PRIVATE)
@Builder
public class MoongchLikesEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long usersId;
  private Long moongchId;
}
