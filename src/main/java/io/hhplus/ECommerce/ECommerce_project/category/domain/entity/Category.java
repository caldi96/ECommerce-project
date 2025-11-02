package io.hhplus.ECommerce.ECommerce_project.category.domain.entity;

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

    public void updateCategoryName(String categoryName) {
        this.categoryName = categoryName;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
        this.updatedAt = LocalDateTime.now();
    }
}
