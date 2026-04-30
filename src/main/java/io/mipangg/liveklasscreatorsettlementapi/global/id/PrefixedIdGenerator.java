package io.mipangg.liveklasscreatorsettlementapi.global.id;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class PrefixedIdGenerator implements IdentifierGenerator {

    // 도메인별 카운터 캐시 - JVM 전역 싱글턴
    private static final ConcurrentHashMap<String, AtomicLong> counters
            = new ConcurrentHashMap<>();

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        String prefix = getPrefix(object);

        AtomicLong counter = counters.computeIfAbsent(prefix, key -> {
            // 해당 prefix 최초 사용 시 딱 1회만 DB에서 현재 MAX 조회
            Long max = queryCurrentMax(session, object, prefix);
            return new AtomicLong(max == null ? 0L : max);
        });

        return prefix + "-" + counter.incrementAndGet();
    }

    private Long queryCurrentMax(
            SharedSessionContractImplementor session,
            Object object, String prefix
    ) {
        String entityName = object.getClass().getName();
        int substringStart = prefix.length() + 2; // "course-" 다음 숫자 시작 위치

        return (Long) session
                .createQuery(
                        "SELECT MAX(CAST(SUBSTRING(e.id, :start) AS long)) " +
                                "FROM " + entityName + " e"
                )
                .setParameter("start", substringStart)
                .getSingleResult();
    }

    private String getPrefix(Object object) {
        IdPrefix annotation = object.getClass().getAnnotation(IdPrefix.class);
        if (annotation != null) return annotation.value();

        return object.getClass().getSimpleName()
                .replaceAll("([A-Z])", "-$1")
                .toLowerCase()
                .replaceFirst("^-", "");
    }
}