package io.hhplus.ECommerce.ECommerce_project.common;

import org.springframework.stereotype.Component;

/**
 * Twitter Snowflake 알고리즘 기반 ID 생성기
 * 64비트 구성:
 * - 1비트: 부호 (항상 0)
 * - 41비트: 타임스탬프 (밀리초)
 * - 10비트: 워커 ID (데이터센터 5비트 + 머신 5비트)
 * - 12비트: 시퀀스 번호
 */
@Component
public class SnowflakeIdGenerator {

    // 시작 시간 (2024-01-01 00:00:00 UTC)
    private static final long EPOCH = 1735689600000L; // 2025-01-01 00:00:00 UTC

    // 각 부분의 비트 수
    private static final long WORKER_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    // 최대값 계산
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS); // 1023
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);    // 4095

    // 비트 이동
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    private final long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    /**
     * 기본 워커 ID = 1로 생성
     */
    public SnowflakeIdGenerator() {
        this(1L);
    }

    /**
     * @param workerId 워커 ID (0 ~ 1023)
     */
    public SnowflakeIdGenerator(long workerId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException(
                String.format("워커 ID는 0부터 %d 사이여야 합니다.", MAX_WORKER_ID)
            );
        }
        this.workerId = workerId;
    }

    /**
     * 새로운 Snowflake ID 생성
     * @return 64비트 Long ID
     */
    public synchronized long nextId() {
        long timestamp = currentTimeMillis();

        // 시간이 역행한 경우 예외 처리
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                String.format("시계가 역행했습니다. %d 밀리초 전으로 되돌아갔습니다.",
                    lastTimestamp - timestamp)
            );
        }

        // 같은 밀리초 내에서 생성된 경우
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 시퀀스 오버플로우 - 다음 밀리초까지 대기
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 새로운 밀리초
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // ID 조합: timestamp | workerId | sequence
        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
            | (workerId << WORKER_ID_SHIFT)
            | sequence;
    }

    /**
     * 현재 시간(밀리초)
     */
    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 다음 밀리초까지 대기
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * Snowflake ID로부터 타임스탬프 추출
     */
    public long extractTimestamp(long id) {
        return (id >> TIMESTAMP_SHIFT) + EPOCH;
    }

    /**
     * Snowflake ID로부터 워커 ID 추출
     */
    public long extractWorkerId(long id) {
        return (id >> WORKER_ID_SHIFT) & MAX_WORKER_ID;
    }

    /**
     * Snowflake ID로부터 시퀀스 추출
     */
    public long extractSequence(long id) {
        return id & MAX_SEQUENCE;
    }
}