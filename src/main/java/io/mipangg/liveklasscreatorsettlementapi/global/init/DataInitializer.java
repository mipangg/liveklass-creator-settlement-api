package io.mipangg.liveklasscreatorsettlementapi.global.init;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity.Cancel;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.repository.CancelRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.entity.CommissionRate;
import io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.repository.CommissionRateRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.repository.CourseRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.repository.CreatorRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.repository.StudentRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final CreatorRepository creatorRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final CancelRepository cancelRepository;
    private final CommissionRateRepository commissionRateRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (creatorRepository.count() > 0) {
            return;
        }

        // JSON 파일 로드
        ClassPathResource resource = new ClassPathResource("sample-data.json");
        JsonNode root = objectMapper.readTree(resource.getInputStream());

        // 1. Creators — json id 무시, name만 사용
        Map<String, Creator> creatorMap = new HashMap<>();
        for (JsonNode node : root.get("creators")) {
            String jsonId = node.get("id").asText(); // 매핑용 키로만 사용
            String name = node.get("name").asText();
            Creator saved = creatorRepository.save(new Creator(name));
            creatorMap.put(jsonId, saved); // json id → 실제 엔티티
        }

        // 2. Students — id만 있으므로 순서대로 매핑
        Map<String, Student> studentMap = new HashMap<>();
        for (JsonNode node : root.get("students")) {
            String jsonId = node.get("id").asText();
            Student saved = studentRepository.save(new Student());
            studentMap.put(jsonId, saved);
        }

        // 3. Courses — creatorId로 위에서 저장한 Creator 참조
        Map<String, Course> courseMap = new HashMap<>();
        for (JsonNode node : root.get("courses")) {
            String jsonId = node.get("id").asText();
            String creatorId = node.get("creatorId").asText();
            String title = node.get("title").asText();
            Creator creator = creatorMap.get(creatorId); // json id로 실제 엔티티 조회
            Course saved = courseRepository.save(new Course(title, creator));
            courseMap.put(jsonId, saved);
        }

        // 4. SaleRecords — _comment, json id 무시
        Map<String, SaleRecord> saleRecordMap = new HashMap<>();
        for (JsonNode node : root.get("saleRecords")) {
            String jsonId = node.get("id").asText();
            Course course = courseMap.get(node.get("courseId").asText());
            Student student = studentMap.get(node.get("studentId").asText());
            BigDecimal amount = BigDecimal.valueOf(node.get("amount").asInt());
            OffsetDateTime paidAt = OffsetDateTime.parse(node.get("paidAt").asText());
            SaleRecord saved = saleRecordRepository.save(new SaleRecord(course, student, amount, paidAt));
            saleRecordMap.put(jsonId, saved);
        }

        // 5. Cancels — saleRecordId로 위에서 저장한 SaleRecord 참조
        for (JsonNode node : root.get("cancels")) {
            SaleRecord saleRecord = saleRecordMap.get(node.get("saleRecordId").asText());
            BigDecimal amount = BigDecimal.valueOf(node.get("amount").asInt());
            OffsetDateTime canceledAt = OffsetDateTime.parse(node.get("canceledAt").asText());
            cancelRepository.save(new Cancel(saleRecord, amount, canceledAt));
        }

        // 6. CommissionRates - commission 생성 후 expireAt() 호출하여 적용 종료 기간 저장
        for (JsonNode node : root.get("commissionRates")) {
            BigDecimal rate = BigDecimal.valueOf(node.get("rate").asInt());
            OffsetDateTime appliedFrom = OffsetDateTime.parse(node.get("appliedFrom").asText());
            CommissionRate commissionRate = new CommissionRate(rate, appliedFrom);

            if (node.get("appliedTo") != null) {
                OffsetDateTime appliedTo = OffsetDateTime.parse(node.get("appliedTo").asText());
                commissionRate.expireAt(appliedTo);
            }
            commissionRateRepository.save(commissionRate);

        }
    }
}