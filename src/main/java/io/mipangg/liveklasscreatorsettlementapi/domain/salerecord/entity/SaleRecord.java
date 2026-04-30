package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import io.mipangg.liveklasscreatorsettlementapi.global.id.IdPrefix;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdPrefix("sale")
@Getter
@Table(
        name = "sale_record",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sale_record_course_id_student_id", // 제약 조건 이름
                        columnNames = {"course_id", "student_id"} // 복합 유니크 키로 묶을 컬럼들
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private OffsetDateTime paidAt;

    @Builder
    public SaleRecord(Course course, Student student, BigDecimal amount, OffsetDateTime paidAt) {
        this.course = course;
        this.student = student;
        this.amount = amount;
        this.paidAt = paidAt;
    }

}
