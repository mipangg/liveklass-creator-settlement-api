package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import io.mipangg.liveklasscreatorsettlementapi.global.TestUtil;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
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

    @Mock
    private DateTimeUtils dateTimeUtils;

    @Test
    @DisplayName("판매 등록을 할 수 있다")
    void saveSaleRecordSuccessTest() {

        SaleRecordCreateRequest req = TestUtil.genSaleRecordCreateRequest();

        Course course = TestUtil.genCourses().getFirst();
        Student student = TestUtil.genStudents().getFirst();

        when(studentRepository.findById(req.studentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(req.courseId())).thenReturn(Optional.of(course));


        saleRecordService.saveSaleRecord(req);

        verify(studentRepository).findById(anyString());
        verify(courseRepository).findById(anyString());
        verify(saleRecordRepository).saveAndFlush(any(SaleRecord.class));

    }

    @Test
    @DisplayName("등록하려는 판매 내역이 이미 존재하면 예외가 발생한다")
    void saveSaleRecordConflictFailTest() {

        SaleRecordCreateRequest req = TestUtil.genSaleRecordCreateRequest();

        when(studentRepository.findById(req.studentId()))
                .thenReturn(Optional.of(TestUtil.genStudents().getFirst()));
        when(courseRepository.findById(req.courseId()))
                .thenReturn(Optional.of(TestUtil.genCourses().getFirst()));

        when(saleRecordRepository.saveAndFlush(any(SaleRecord.class)))
                .thenThrow(new DataIntegrityViolationException(""));

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

        SaleRecordCreateRequest req = TestUtil.genSaleRecordCreateRequest();

        Student student = TestUtil.genStudents().getFirst();

        when(studentRepository.findById(req.studentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(req.courseId())).thenReturn(Optional.empty());

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

    @Test
    @DisplayName("필터링 없이 전체 판매 내역 목록을 조회할 수 있다")
    void findSaleRecordsSuccessTest() {

        SaleRecordListReadRequest req = new SaleRecordListReadRequest(null, null, null);
        List<SaleRecord> saleRecords = TestUtil.genSaleRecords();

        when(saleRecordRepository.findSaleRecords(any(), any(), any())).thenReturn(saleRecords);

        List<SaleRecordListReadResponse> resp = saleRecordService.findSaleRecords(req);

        assertThat(resp).hasSize(saleRecords.size());
        for (int i = 0; i < resp.size(); i++) {
            assertThat(resp.get(i).courseId()).isEqualTo(saleRecords.get(i).getCourse().getId());
            assertThat(resp.get(i).studentId()).isEqualTo(saleRecords.get(i).getStudent().getId());
            assertThat(resp.get(i).amount()).isEqualTo(saleRecords.get(i).getAmount());
            assertThat(resp.get(i).paidAt()).isEqualTo(saleRecords.get(i).getPaidAt());
        }
    }
    
    @Test
    @DisplayName("크리에이터별 판매 내역 목록을 조회할 수 있다")
    void findSaleRecordsByCreatorSuccessTest() {

        SaleRecordListReadRequest req = new SaleRecordListReadRequest("creator-1", null, null);

        List<Course> courses = TestUtil.genCourses();
        List<Student> students = TestUtil.genStudents();

        List<SaleRecord> saleRecords = List.of(
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
                        .build()
        );

        when(saleRecordRepository.findSaleRecords(any(), any(), any())).thenReturn(saleRecords);

        List<SaleRecordListReadResponse> resp = saleRecordService.findSaleRecords(req);

        assertThat(resp).hasSize(saleRecords.size());
        for (int i = 0; i < resp.size(); i++) {
            assertThat(resp.get(i).courseId()).isEqualTo(saleRecords.get(i).getCourse().getId());
            assertThat(resp.get(i).studentId()).isEqualTo(saleRecords.get(i).getStudent().getId());
            assertThat(resp.get(i).amount()).isEqualTo(saleRecords.get(i).getAmount());
            assertThat(resp.get(i).paidAt()).isEqualTo(saleRecords.get(i).getPaidAt());
        }

    }
    
    @Test
    @DisplayName("기간별 판매 내역 목록을 조회할 수 있다")
    void findSaleRecordsByStartDateAndEndDateSuccessTest() {

        SaleRecordListReadRequest req = new SaleRecordListReadRequest(
                null,
                LocalDate.parse("2025-03-05"),
                LocalDate.parse("2025-03-10")
        );

        List<Course> courses = TestUtil.genCourses();
        List<Student> students = TestUtil.genStudents();

        List<SaleRecord> saleRecords = List.of(
                SaleRecord.builder()
                        .course(courses.get(0))
                        .student(students.get(0))
                        .amount(BigDecimal.valueOf(50000))
                        .paidAt(OffsetDateTime.parse("2025-03-05T10:00:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(2))
                        .student(students.get(5))
                        .amount(BigDecimal.valueOf(60000))
                        .paidAt(OffsetDateTime.parse("2025-03-10T16:00:00+09:00"))
                        .build()
        );

        OffsetDateTime start = OffsetDateTime.parse("2025-03-05T00:00:00+09:00");
        OffsetDateTime end = OffsetDateTime.parse("2025-03-10T23:59:59+09:00");

        when(dateTimeUtils.toStartDateTime(req.startDate())).thenReturn(start);
        when(dateTimeUtils.toEndDateTime(req.endDate())).thenReturn(end);
        when(saleRecordRepository.findSaleRecords(req.creatorId(), start, end)).thenReturn(saleRecords);

        List<SaleRecordListReadResponse> resp = saleRecordService.findSaleRecords(req);

        assertThat(resp).hasSize(saleRecords.size());
        for (int i = 0; i < resp.size(); i++) {
            assertThat(resp.get(i).courseId()).isEqualTo(saleRecords.get(i).getCourse().getId());
            assertThat(resp.get(i).studentId()).isEqualTo(saleRecords.get(i).getStudent().getId());
            assertThat(resp.get(i).amount()).isEqualTo(saleRecords.get(i).getAmount());
            assertThat(resp.get(i).paidAt()).isEqualTo(saleRecords.get(i).getPaidAt());
        }
    
    }
    
    @Test
    @DisplayName("크리에이터별 + 기간별 판매 내역 목록을 조회할 수 있다")
    void findSaleRecordsByCreatorAndStartDateAndEndDateSuccessTest() {

        SaleRecordListReadRequest req = new SaleRecordListReadRequest(
                "creator-1",
                LocalDate.parse("2025-03-05"),
                LocalDate.parse("2025-03-10")
        );

        Course course = TestUtil.genCourses().getFirst();
        Student student = TestUtil.genStudents().getFirst();

        List<SaleRecord> saleRecords = List.of(
                SaleRecord.builder()
                        .course(course)
                        .student(student)
                        .amount(BigDecimal.valueOf(50000))
                        .paidAt(OffsetDateTime.parse("2025-03-05T10:00:00+09:00"))
                        .build()
        );

        OffsetDateTime start = OffsetDateTime.parse("2025-03-05T00:00:00+09:00");
        OffsetDateTime end = OffsetDateTime.parse("2025-03-10T23:59:59+09:00");

        when(dateTimeUtils.toStartDateTime(req.startDate())).thenReturn(start);
        when(dateTimeUtils.toEndDateTime(req.endDate())).thenReturn(end);
        when(saleRecordRepository.findSaleRecords(req.creatorId(), start, end)).thenReturn(saleRecords);

        List<SaleRecordListReadResponse> resp = saleRecordService.findSaleRecords(req);

        assertThat(resp).hasSize(saleRecords.size());
        for (int i = 0; i < resp.size(); i++) {
            assertThat(resp.get(i).courseId()).isEqualTo(saleRecords.get(i).getCourse().getId());
            assertThat(resp.get(i).studentId()).isEqualTo(saleRecords.get(i).getStudent().getId());
            assertThat(resp.get(i).amount()).isEqualTo(saleRecords.get(i).getAmount());
            assertThat(resp.get(i).paidAt()).isEqualTo(saleRecords.get(i).getPaidAt());
        }
    
    }

    @Test
    @DisplayName("필터링 후 조회되는 판매 내역 목록이 없으면 빈 리스트를 반환한다")
    void findSaleRecordsEmptyListSuccessTest() {

        SaleRecordListReadRequest req = new SaleRecordListReadRequest(
                "creator-4",
                LocalDate.parse("2025-03-05"),
                LocalDate.parse("2025-03-10")
        );

        List<SaleRecord> saleRecords = List.of();

        OffsetDateTime start = OffsetDateTime.parse("2025-03-05T00:00:00+09:00");
        OffsetDateTime end = OffsetDateTime.parse("2025-03-10T23:59:59+09:00");

        when(dateTimeUtils.toStartDateTime(req.startDate())).thenReturn(start);
        when(dateTimeUtils.toEndDateTime(req.endDate())).thenReturn(end);
        when(saleRecordRepository.findSaleRecords(req.creatorId(), start, end)).thenReturn(saleRecords);

        List<SaleRecordListReadResponse> resp = saleRecordService.findSaleRecords(req);

        assertThat(resp).hasSize(0);
    }
    
    @Test
    @DisplayName("SaleRecordListReadRequest 속 start/endDate 유효성 검증에 실패하면 예외가 발생한다")
    void findSaleRecordsByStartDateAndEndDateFailTest() {

        assertThatThrownBy(
                () -> {
                    new SaleRecordListReadRequest(
                            "creator-1",
                            LocalDate.parse("2025-03-15"),
                            LocalDate.parse("2025-03-10")
                    );
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage(ErrorCode.INVALID_DATE_RANGE.getMessage());
    
    }
    
}