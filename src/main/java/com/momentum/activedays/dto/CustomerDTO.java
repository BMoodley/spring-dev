package com.momentum.activedays.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CustomerDTO {
    private String id;
    private String name;
    private Integer points;
}
