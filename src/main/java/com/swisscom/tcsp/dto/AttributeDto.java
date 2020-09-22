package com.swisscom.tcsp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of = {"name", "value", "operation"}, callSuper = false)
@JsonPropertyOrder({"name", "value", "operation"})
public class AttributeDto implements Serializable {
    private String name;
    private String value;
    private Operation operation;

}
