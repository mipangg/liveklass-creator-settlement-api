package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.service;

import static io.mipangg.liveklasscreatorsettlementapi.global.TestUtil.genCourses;
import static io.mipangg.liveklasscreatorsettlementapi.global.TestUtil.genSaleRecordCreateRequest;
import static io.mipangg.liveklasscreatorsettlementapi.global.TestUtil.genStudents;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.repository.CourseRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.repository.StudentRepository;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class SaleRecordServiceTests {

    @InjectMocks
    private SaleRecordService saleRecordService;

    @Mock
    private SaleRecordRepository saleRecordRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Test
    @DisplayName("판매 등록을 할 수 있다")
    void saveSaleRecordSuccessTest() {

        SaleRecordCreateRequest req = genSaleRecordCreateRequest();

        Course course = genCourses().getFirst();
        Student student = genStudents().getFirst();

        when(studentRepository.findById(anyString())).thenReturn(Optional.of(student));
        when(courseRepository.findById(anyString())).thenReturn(Optional.of(course));

        saleRecordService.saveSaleRecord(req);

        verify(studentRepository).findById(anyString());
        verify(courseRepository).findById(anyString());
        verify(saleRecordRepository).save(any(SaleRecord.class));

    }

    @Test
    @DisplayName("등록하려는 판매 내역이 이미 존재하면 예외가 발생한다")
    void saveSaleRecordConflictFailTest() {

        SaleRecordCreateRequest req = genSaleRecordCreateRequest();

        when(studentRepository.findById(anyString()))
                .thenReturn(Optional.of(genStudents().getFirst()));
        when(courseRepository.findById(anyString()))
                .thenReturn(Optional.of(genCourses().getFirst()));

        when(saleRecordRepository.save(any())).thenThrow(new DataIntegrityViolationException(""));

        assertThatThrownBy(
                () -> {
                    saleRecordService.saveSaleRecord(req);
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage(ErrorCode.SALE_RECORD_CONFLICT.getMessage());

    }

    @Test
    @DisplayName("존재하지 않는 Course로 판매 내역 등록을 시도하면 예외가 발생한다")
    void saveSaleRecordNotFoundFailTest() {

        SaleRecordCreateRequest req = genSaleRecordCreateRequest();

        Student student = genStudents().getFirst();

        when(studentRepository.findById(anyString())).thenReturn(Optional.of(student));
        when(courseRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> {
                    saleRecordService.saveSaleRecord(req);
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage(ErrorCode.COURSE_NOT_FOUND.getMessage());

        verify(studentRepository).findById(anyString());
        verify(courseRepository).findById(anyString());
        verify(saleRecordRepository, never()).save(any(SaleRecord.class));

    }

}