package com.Hieu2k3.course.dtos.responses.user;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfoTeacherResponse {

    Long id;
    String author;
    Integer reviewAmount;
    BigDecimal rating;
    Integer courseAmount;
    String description;
}
