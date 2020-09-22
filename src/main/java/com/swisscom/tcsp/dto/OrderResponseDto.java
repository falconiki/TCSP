package com.swisscom.tcsp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "identifier", "brickId", "operation", "attributes", "children" })
public class OrderResponseDto implements Serializable {
    private String identifier;
    private Type brickId;
    private Operation operation;
    private Collection<AttributeDto> attributes;
    private Collection<ChildDto> children;

}
