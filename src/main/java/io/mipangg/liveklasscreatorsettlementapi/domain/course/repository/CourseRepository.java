package io.mipangg.liveklasscreatorsettlementapi.domain.course.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findById(String id);

}
