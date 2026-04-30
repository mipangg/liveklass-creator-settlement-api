package io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Creator extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String publicId;

    @Column(nullable = false)
    private String name;

    @Builder
    public Creator(String publicId, String name) {
        this.publicId = publicId;
        this.name = name;
    }

}
