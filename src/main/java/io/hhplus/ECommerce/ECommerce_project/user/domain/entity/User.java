package io.hhplus.ECommerce.ECommerce_project.user.domain.entity;

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
public class User {

    private Long id;                    // pk
    private String username;            // 로그인 id
    private String password;            // 로그인 password
    private BigDecimal pointBalance;    // 포인트 잔액
    private LocalDateTime createdAt;    // 생성일
    private LocalDateTime updatedAt;    // 수정일

    public boolean hasPoint(BigDecimal usePoint) {
        // compareTo: 0 = 같음, 1 = this > usePoint, -1 = this < usePoint
        return this.pointBalance.compareTo(usePoint) >= 0;
    }

    public void usePoint(BigDecimal usePoint) {
        if (!hasPoint(usePoint)) throw new IllegalArgumentException("포인트가 부족합니다.");
        this.pointBalance = this.pointBalance.subtract(usePoint);
        this.updatedAt = LocalDateTime.now();
    }

    public void chargePoint(BigDecimal chargePoint) {
        this.pointBalance = this.pointBalance.add(chargePoint);
        this.updatedAt = LocalDateTime.now();
    }
}
