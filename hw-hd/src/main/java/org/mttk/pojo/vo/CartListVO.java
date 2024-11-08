package org.mttk.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartListVO {
    private Integer cartId;
    private String title;
    private String author;
    private String description;
    private Long price;
    private Integer quantity;
    private Long totalPrice;
}
