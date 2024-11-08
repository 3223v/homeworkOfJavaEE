package org.mttk.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListVO {
    private Integer orderId;
    private String title;
    private String author;
    private Long price;
    private Integer quantity;
    private Long totalPrice;
}
