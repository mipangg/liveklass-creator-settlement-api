package io.mipangg.liveklasscreatorsettlementapi.domain.course.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
