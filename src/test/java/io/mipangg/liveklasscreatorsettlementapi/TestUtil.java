package io.mipangg.liveklasscreatorsettlementapi;

import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.controller.SaleRecordController;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestUtil {

    public static List<Creator> genCreators() {
        return List.of(
                Creator.builder()
                        .publicId("creator-1")
                        .name("김강사")
                        .build(),
                Creator.builder()
                        .publicId("creator-2")
                        .name("이강사")
                        .build(),
                Creator.builder()
                        .publicId("creator-3")
                        .name("박강사")
                        .build()
        );
    }

    public static List<Student> genStudents() {
        return List.of(
                Student.builder()
                        .publicId("student-1")
                        .build(),
                Student.builder()
                        .publicId("student-2")
                        .build(),
                Student.builder()
                        .publicId("student-3")
                        .build(),
                Student.builder()
                        .publicId("student-4")
                        .build(),
                Student.builder()
                        .publicId("student-5")
                        .build(),
                Student.builder()
                        .publicId("student-6")
                        .build(),
                Student.builder()
                        .publicId("student-7")
                        .build()
        );
    }

    public static List<Course> genCourses() {
        List<Creator> creators = genCreators();
        return List.of(
                Course.builder()
                        .publicId("course-1")
                        .creator(creators.get(0))
                        .title("Spring Boot 입문")
                        .build(),
                Course.builder()
                        .publicId("course-2")
                        .creator(creators.get(0))
                        .title("JPA 실전")
                        .build(),
                Course.builder()
                        .publicId("course-3")
                        .creator(creators.get(1))
                        .title("Kotlin 기초")
                        .build(),
                Course.builder()
                        .publicId("course-4")
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
                        .publicId("sale-1")
                        .course(courses.get(0))
                        .student(students.get(0))
                        .amount(BigDecimal.valueOf(5000))
                        .paidAt(LocalDateTime.of(2025, 3, 5, 10, 0))
                        .build(),
                SaleRecord.builder()
                        .publicId("sale-2")
                        .course(courses.get(0))
                        .student(students.get(1))
                        .amount(BigDecimal.valueOf(50000))
                        .paidAt(LocalDateTime.of(2025, 3, 15, 14,30))
                        .build(),
                SaleRecord.builder()
                        .publicId("sale-3")
                        .course(courses.get(1))
                        .student(students.get(2))
                        .amount(BigDecimal.valueOf(80000))
                        .paidAt(LocalDateTime.of(2025, 3, 20, 9, 0))
                        .build(),
                SaleRecord.builder()
                        .publicId("sale-4")
                        .course(courses.get(1))
                        .student(students.get(3))
                        .amount(BigDecimal.valueOf(80000))
                        .paidAt(LocalDateTime.of(2025, 3, 22, 11, 0))
                        .build(),
                SaleRecord.builder()
                        .publicId("sale-5")
                        .course(courses.get(2))
                        .student(students.get(4))
                        .amount(BigDecimal.valueOf(60000))
                        .paidAt(LocalDateTime.of(2025, 1, 31, 23, 30))
                        .build(),
                SaleRecord.builder()
                        .publicId("sale-6")
                        .course(courses.get(2))
                        .student(students.get(5))
                        .amount(BigDecimal.valueOf(60000))
                        .paidAt(LocalDateTime.of(2025, 3, 10, 16, 0))
                        .build(),
                SaleRecord.builder()
                        .publicId("sale-7")
                        .course(courses.get(5))
                        .student(students.get(6))
                        .amount(BigDecimal.valueOf(120000))
                        .paidAt(LocalDateTime.of(2025, 2, 14, 10, 0))
                        .build()
        );
    }
}
