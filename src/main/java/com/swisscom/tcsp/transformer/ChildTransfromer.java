package com.swisscom.tcsp.transformer;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.swisscom.tcsp.dto.AttributeDto;
import com.swisscom.tcsp.dto.ChildDto;
import com.swisscom.tcsp.dto.Operation;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ChildTransfromer {

    public static List<ChildDto> mergeChildren(MapDifference<String, ChildDto> crossCheckChildren) {
        log.info("Merging children elements");
        List<ChildDto> childrenDto = mergeCommonAndUniqueChildren(crossCheckChildren);

        Collection<MapDifference.ValueDifference<ChildDto>> values = crossCheckChildren.entriesDiffering().values();
        for (MapDifference.ValueDifference<ChildDto> value : values) {
            ChildDto.ChildDtoBuilder childDtoBuilder = ChildDto.builder().attributes(new ArrayList<>());
            childDtoBuilder.id(value.rightValue().getId()).type(value.rightValue().getType()).operation(Operation.UPDATE).relations(value.rightValue().getRelations());
            Map<String, AttributeDto> sourceAttributeDto = value.leftValue().getAttributes().stream().collect(Collectors.toMap(AttributeDto::getName, child -> child, (a, b) -> b));
            Map<String, AttributeDto> targetAttributeDto = value.rightValue().getAttributes().stream().collect(Collectors.toMap(AttributeDto::getName, child -> child, (a, b) -> b));
            MapDifference<String, AttributeDto> crossCheckAttributes = Maps.difference(sourceAttributeDto, targetAttributeDto);
            if (value.leftValue().getId().equals(value.rightValue().getId()) && value.leftValue().getType().equals(value.rightValue().getType())) {
                Collection<AttributeDto> attributeDtoList = AttributesTransformer.mergeAttributes(crossCheckAttributes);
                childDtoBuilder.attributes(attributeDtoList);
                childrenDto.add(childDtoBuilder.build());
            }
        }
        return childrenDto;
    }

    private static List<ChildDto> mergeCommonAndUniqueChildren(MapDifference<String, ChildDto> crossCheckChildren) {
        setOperationToChild(crossCheckChildren.entriesOnlyOnLeft(), Operation.DELETE);
        setOperationToChild(crossCheckChildren.entriesOnlyOnRight(), Operation.CREATE);
        setOperationToChild(crossCheckChildren.entriesInCommon(), Operation.NO_ACTION);
        return Stream.of(crossCheckChildren.entriesOnlyOnRight().values(), crossCheckChildren.entriesOnlyOnLeft().values(), crossCheckChildren.entriesInCommon().values())
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
    }

    private static void setOperationToChild(Map<String, ChildDto> childDtoMap, Operation operation) {
        childDtoMap.values().stream().forEach(m -> {
            m.setOperation(operation);
            m.getAttributes().stream().forEach(a -> a.setOperation(operation));
        });
    }

}
