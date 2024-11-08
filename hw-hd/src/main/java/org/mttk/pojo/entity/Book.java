package org.mttk.pojo.entity;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer id;
    private String title;
    private String author;
    private String description;
    private Long price;
    private Integer stock;
}
