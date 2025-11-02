package io.hhplus.ECommerce.ECommerce_project.point.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.point.domain.enums.PointType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    private Long id;

    // 나중에 JPA 연결 시
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id")
    //private User user;
    private Long userId;
    private BigDecimal amount;
    private PointType pointType;
    private String description;

    // 나중에 JPA 연결 시
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "order_id")
    //private Order order;
    private Long orderId;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    /*
    public Point charge(String userId, BigDecimal amount, String description) {

    }
     */
}
