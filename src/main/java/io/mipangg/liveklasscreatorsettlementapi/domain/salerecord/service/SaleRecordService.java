package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.service;

import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.repository.CourseRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.repository.StudentRepository;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaleRecordService {

    private final SaleRecordRepository saleRecordRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void saveSaleRecord(SaleRecordCreateRequest req) {

        if (saleRecordRepository.existsSaleRecordByCourseIdAndStudentId(
                        req.courseId(),
                        req.studentId()
        )) {
            throw new CustomLogicException(ErrorCode.SALE_RECORD_CONFLICT);
        }

        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new CustomLogicException(ErrorCode.STUDENT_NOT_FOUND));

        Course course = courseRepository.findById(req.courseId())
                .orElseThrow(() -> new CustomLogicException(ErrorCode.COURSE_NOT_FOUND));


        saleRecordRepository.save(new SaleRecord(course, student, req.amount(), req.paidAt()));

    }
}
