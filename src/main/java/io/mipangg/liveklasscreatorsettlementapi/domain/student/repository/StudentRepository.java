package io.mipangg.liveklasscreatorsettlementapi.domain.student.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
