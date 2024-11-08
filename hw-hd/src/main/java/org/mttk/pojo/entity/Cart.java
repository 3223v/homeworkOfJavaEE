package org.mttk.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Integer id;
    private Integer user_id;
    private Integer book_id;
    private Integer quantity;
}
