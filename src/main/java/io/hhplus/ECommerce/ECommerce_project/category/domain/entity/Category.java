package io.hhplus.ECommerce.ECommerce_project.category.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.common.exception.CategoryException;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long id;
    private String categoryName;
    private int displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 카테고리명 수정
     */
    public void updateCategoryName(String name) {
        validateName(name);
        this.categoryName = name;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 표시 순서 변경
     * 주의: displayOrder 중복 검증은 Service에서 수행해야 함
     */
    public void updateDisplayOrder(int displayOrder) {
        if (displayOrder <= 0) {
            throw new CategoryException(ErrorCode.DISPLAY_ORDER_INVALID);
        }
        this.displayOrder = displayOrder;
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Validation 메서드 =====

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new CategoryException(ErrorCode.CATEGORY_NAME_REQUIRED);
        }
    }
}
