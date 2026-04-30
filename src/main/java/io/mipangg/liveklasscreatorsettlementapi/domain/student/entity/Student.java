package io.mipangg.liveklasscreatorsettlementapi.domain.student.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import io.mipangg.liveklasscreatorsettlementapi.global.id.IdPrefix;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdPrefix("student")
@Getter
@NoArgsConstructor
public class Student extends BaseEntity {

}
