package io.mipangg.liveklasscreatorsettlementapi.global;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.dto.CancelCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordListReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class TestUtil {

    public static List<Creator> genCreators() {
        return List.of(
                Creator.builder()
                        .name("김강사")
                        .build(),
                Creator.builder()
                        .name("이강사")
                        .build(),
                Creator.builder()
                        .name("박강사")
                        .build()
        );
    }

    public static List<Student> genStudents() {
        return List.of(
                new Student(),
                new Student(),
                new Student(),
                new Student(),
                new Student(),
                new Student(),
                new Student()
        );
    }

    public static List<Course> genCourses() {
        List<Creator> creators = genCreators();
        return List.of(
                Course.builder()
                        .creator(creators.get(0))
                        .title("Spring Boot 입문")
                        .build(),
                Course.builder()
                        .creator(creators.get(0))
                        .title("JPA 실전")
                        .build(),
                Course.builder()
                        .creator(creators.get(1))
                        .title("Kotlin 기초")
                        .build(),
                Course.builder()
                        .creator(creators.get(2))
                        .title("MSA 설계")
                        .build()
        );
    }

    public static List<SaleRecord> genSaleRecords() {
        List<Course> courses = genCourses();
        List<Student> students = genStudents();
        return List.of(
                SaleRecord.builder()
                        .course(courses.get(0))
                        .student(students.get(0))
                        .amount(BigDecimal.valueOf(50000))
                        .paidAt(OffsetDateTime.parse("2025-03-05T10:00:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(0))
                        .student(students.get(1))
                        .amount(BigDecimal.valueOf(50000))
                        .paidAt(OffsetDateTime.parse("2025-03-15T14:30:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(1))
                        .student(students.get(2))
                        .amount(BigDecimal.valueOf(80000))
                        .paidAt(OffsetDateTime.parse("2025-03-20T09:00:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(1))
                        .student(students.get(3))
                        .amount(BigDecimal.valueOf(80000))
                        .paidAt(OffsetDateTime.parse("2025-03-22T11:00:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(2))
                        .student(students.get(4))
                        .amount(BigDecimal.valueOf(60000))
                        .paidAt(OffsetDateTime.parse("2025-01-31T23:30:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(2))
                        .student(students.get(5))
                        .amount(BigDecimal.valueOf(60000))
                        .paidAt(OffsetDateTime.parse("2025-03-10T16:00:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(3))
                        .student(students.get(6))
                        .amount(BigDecimal.valueOf(120000))
                        .paidAt(OffsetDateTime.parse("2025-02-14T10:00:00+09:00"))
                        .build()
        );
    }

    public static SaleRecordCreateRequest genSaleRecordCreateRequest() {
        return new SaleRecordCreateRequest(
                "course-1",
                "student-1",
                BigDecimal.valueOf(50000),
                OffsetDateTime.parse("2025-03-05T10:00:00+09:00")
        );
    }

    public static CancelCreateRequest genCancelCreateRequest() {
        return new CancelCreateRequest(
                "sale-3",
                BigDecimal.valueOf(80000),
                OffsetDateTime.parse("2025-03-21T09:00:00+09:00")
        );
    }

    public static List<SaleRecordListReadResponse> genSaleRecordListReadResponses() {
        return List.of(
                new SaleRecordListReadResponse(
                        "sale-1",
                        "course-1",
                        "student-1",
                        BigDecimal.valueOf(50000),
                        OffsetDateTime.parse("2025-03-05T10:00:00+09:00")
                ),
                new SaleRecordListReadResponse(
                        "sale-2",
                        "course-1",
                        "student-2",
                        BigDecimal.valueOf(50000),
                        OffsetDateTime.parse("2025-03-15T14:30:00+09:00")
                ),
                new SaleRecordListReadResponse(
                        "sale-3",
                        "course-2",
                        "student-3",
                        BigDecimal.valueOf(80000),
                        OffsetDateTime.parse("2025-03-20T09:00:00+09:00")
                ),
                new SaleRecordListReadResponse(
                        "sale-4",
                        "course-2",
                        "student-4",
                        BigDecimal.valueOf(80000),
                        OffsetDateTime.parse("2025-03-22T11:00:00+09:00")
                ),
                new SaleRecordListReadResponse(
                        "sale-5",
                        "course-3",
                        "student-5",
                        BigDecimal.valueOf(60000),
                        OffsetDateTime.parse("2025-01-31T23:30:00+09:00")
                ),
                new SaleRecordListReadResponse(
                        "sale-6",
                        "course-3",
                        "student-6",
                        BigDecimal.valueOf(60000),
                        OffsetDateTime.parse("2025-03-10T16:00:00+09:00")
                ),
                new SaleRecordListReadResponse(
                        "sale-7",
                        "course-4",
                        "student-7",
                        BigDecimal.valueOf(120000),
                        OffsetDateTime.parse("2025-02-14T10:00:00+09:00")
                )
        );
    }
}
