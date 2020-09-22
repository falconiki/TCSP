package com.swisscom.tcsp.dto;

import lombok.*;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode(of = {"id", "type"})
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    private String id;
    private Type type;
    private List<AttributeDto> attributes;
    private List<ChildDto> children;
}
