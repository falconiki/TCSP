package com.swisscom.tcsp.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;

@Data
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "operation", "attributes", "children"})
public class ChildDto implements Serializable {

    private String id;
    private String type;
    private Collection<AttributeDto> attributes;
    private Collection<String> relations;
    private Operation operation;

    @JsonGetter(value = "identifier")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter(value = "brickId")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<AttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<AttributeDto> attributes) {
        this.attributes = attributes;
    }

    public Collection<String> getRelations() {
        return relations;
    }

    public void setRelations(Collection<String> relations) {
        this.relations = relations;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
