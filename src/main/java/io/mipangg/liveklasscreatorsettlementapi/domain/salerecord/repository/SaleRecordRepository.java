package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRecordRepository extends JpaRepository<SaleRecord, Long> {

    boolean existsSaleRecordByCourseIdAndStudentId(String courseId, String studentId);

}
