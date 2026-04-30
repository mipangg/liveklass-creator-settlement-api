package io.mipangg.liveklasscreatorsettlementapi.domain.student.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findById(String id);

}
