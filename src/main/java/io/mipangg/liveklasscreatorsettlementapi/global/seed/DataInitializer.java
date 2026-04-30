package io.mipangg.liveklasscreatorsettlementapi.global.seed;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.repository.CourseRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.repository.CreatorRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CreatorRepository creatorRepository;
    private final CourseRepository courseRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        InputStream is = new ClassPathResource("sample-data.json").getInputStream();

        SampleData data = objectMapper.readValue(is, SampleData.class);

        creatorRepository.saveAll(data.getCreators());
        courseRepository.saveAll(data.getCourses());
        saleRecordRepository.saveAll(data.getSaleRecords());
    }
}
