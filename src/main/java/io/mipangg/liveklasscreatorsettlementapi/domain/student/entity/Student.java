package io.mipangg.liveklasscreatorsettlementapi.domain.student.entity;

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
public class Student extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String publicId;

    @Builder
    public Student(String publicId) {
        this.publicId = publicId;
    }

}
