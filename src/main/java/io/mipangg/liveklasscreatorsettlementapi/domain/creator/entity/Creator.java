package io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import io.mipangg.liveklasscreatorsettlementapi.global.id.IdPrefix;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdPrefix("creator")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Creator extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Builder
    public Creator(String name) {
        this.name = name;
    }

}
