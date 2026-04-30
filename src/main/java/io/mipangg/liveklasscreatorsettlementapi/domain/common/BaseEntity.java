package io.mipangg.liveklasscreatorsettlementapi.domain.common;

import io.mipangg.liveklasscreatorsettlementapi.global.id.IdPrefix;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseEntity를 상속할 경우 필드들도 칼럼으로 인식
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 포함
public abstract class BaseEntity {

    @Id
    @IdPrefix("")
    private String id;

    @CreatedDate // 생성일 자동 저장
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정일 자동 저장
    private LocalDateTime updatedAt;

}
