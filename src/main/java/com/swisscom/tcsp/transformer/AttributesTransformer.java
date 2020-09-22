package com.swisscom.tcsp.transformer;

import com.google.common.collect.MapDifference;
import com.swisscom.tcsp.dto.AttributeDto;
import com.swisscom.tcsp.dto.Operation;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class AttributesTransformer {

    public static List<AttributeDto> compareAndMergeAttributes(MapDifference<String, AttributeDto> crosscheckAttributes) {
        Collection<AttributeDto> attributes;
        if (crosscheckAttributes.areEqual()) {
            crosscheckAttributes.entriesInCommon().values().forEach(a -> a.setOperation(Operation.NO_ACTION));
            attributes = crosscheckAttributes.entriesInCommon().values();
        } else {
            attributes = mergeAttributes(crosscheckAttributes);
        }
        return new ArrayList<>(attributes);
    }

    public static List<AttributeDto> mergeAttributes(MapDifference<String, AttributeDto> crosscheckAttributes) {
        log.info("Merging attributes elements ");
        setOperationToAttributes(crosscheckAttributes.entriesInCommon(), Operation.NO_ACTION);
        setOperationToAttributes(crosscheckAttributes.entriesOnlyOnLeft(), Operation.DELETE);
        setOperationToAttributes(crosscheckAttributes.entriesOnlyOnRight(), Operation.CREATE);
        crosscheckAttributes.entriesDiffering().values().stream().forEach(m -> m.rightValue().setOperation(Operation.UPDATE));

        List<AttributeDto> attributeDtoList = Stream.of(crosscheckAttributes.entriesOnlyOnRight().values(), crosscheckAttributes.entriesOnlyOnLeft().values(),
                crosscheckAttributes.entriesInCommon().values())
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
        attributeDtoList.addAll(crosscheckAttributes.entriesDiffering().values().stream().map(m -> m.rightValue()).collect(Collectors.toList()));
        return attributeDtoList;
    }

    public static void setOperationToAttributes(Map<String, AttributeDto> stringAttributeDtoMap, Operation noAction) {
        stringAttributeDtoMap.values().stream().forEach(m -> m.setOperation(noAction));
    }

}
