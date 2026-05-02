package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.service;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.util.DateTimeUtils;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.repository.CourseRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordListReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordListReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.repository.StudentRepository;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaleRecordService {

    private final SaleRecordRepository saleRecordRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    private final DateTimeUtils dateTimeUtils;

    @Transactional
    public void saveSaleRecord(SaleRecordCreateRequest req) {

        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new CustomLogicException(ErrorCode.STUDENT_NOT_FOUND));

        Course course = courseRepository.findById(req.courseId())
                .orElseThrow(() -> new CustomLogicException(ErrorCode.COURSE_NOT_FOUND));


        try {
            saleRecordRepository.saveAndFlush(
                    new SaleRecord(course, student, req.amount(), req.paidAt())
            );
        } catch (DataIntegrityViolationException e) { // DB 제약조건 위반
            throw new CustomLogicException(ErrorCode.SALE_RECORD_CONFLICT);
        }

    }

    @Transactional(readOnly = true)
    public List<SaleRecordListReadResponse> findSaleRecords(SaleRecordListReadRequest req) {

        OffsetDateTime start = null;
        OffsetDateTime end = null;

        if (req.startDate() != null && req.endDate() != null) {
            start = dateTimeUtils.toStartDateTime(req.startDate());
            end = dateTimeUtils.toEndDateTime(req.endDate());
        }

        List<SaleRecord> saleRecords = saleRecordRepository.findSaleRecords(
                req.creatorId(),
                start,
                end
        );

        List<SaleRecordListReadResponse> resps = new ArrayList<>();
        for (SaleRecord saleRecord : saleRecords) {
            resps.add(
                    new SaleRecordListReadResponse(
                            saleRecord.getId(),
                            saleRecord.getCourse().getId(),
                            saleRecord.getStudent().getId(),
                            saleRecord.getAmount(),
                            saleRecord.getPaidAt()
                    )
            );
        }

        return resps;
    }
}
