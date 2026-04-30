package io.mipangg.liveklasscreatorsettlementapi.global.seed;

import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import java.util.List;
import lombok.Data;

@Data
public class SampleData {
    List<Creator> creators;
    List<Course> courses;
    List<SaleRecord> saleRecords;
}
