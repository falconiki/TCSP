package com.swisscom.tcsp.transformer;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.swisscom.tcsp.dto.*;
import com.swisscom.tcsp.dto.AttributeDto;
import com.swisscom.tcsp.dto.ChildDto;
import com.swisscom.tcsp.dto.OrderRequestDto;
import com.swisscom.tcsp.dto.OrderResponseDto;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.swisscom.tcsp.Constants.JsonFileUtilConstants.*;

@Slf4j
public class OrderTreeTransformer {

    private static Gson gson = new GsonBuilder().create();

    /**
     * Method that transforms initial and new tree and merged into one result
     *
     * @param orderRequestSourceDto
     * @param orderRequestTargetDto
     * @return
     */
    public static OrderResponseDto transformTreesIntoResultOrder(OrderRequestDto orderRequestSourceDto, OrderRequestDto orderRequestTargetDto) {
        log.info("Transformation of trees with id: [{}] will start", orderRequestSourceDto.getId());
        Map<String, OrderRequestDto> sourceRoot = Map.of(orderRequestSourceDto.getId(), orderRequestSourceDto);
        Map<String, OrderRequestDto> targetRoot = Map.of(orderRequestTargetDto.getId(), orderRequestTargetDto);
        Map<String, ChildDto> sourceChildren = orderRequestSourceDto.getChildren().stream().collect(Collectors.toMap(ChildDto::getId, child -> child, (a, b) -> b));
        Map<String, ChildDto> targetChildren = orderRequestTargetDto.getChildren().stream().collect(Collectors.toMap(ChildDto::getId, child -> child, (a, b) -> b));
        Map<String, AttributeDto> sourceAttributes = orderRequestSourceDto.getAttributes().stream().collect(Collectors.toMap(AttributeDto::getName, child -> child, (a, b) -> b));
        Map<String, AttributeDto> targetAttributes = orderRequestTargetDto.getAttributes().stream().collect(Collectors.toMap(AttributeDto::getName, child -> child, (a, b) -> b));

        MapDifference<String, OrderRequestDto> crosscheckRoot = Maps.difference(sourceRoot, targetRoot);
        MapDifference<String, AttributeDto> crosscheckAttributes = Maps.difference(sourceAttributes, targetAttributes);
        MapDifference<String, ChildDto> crosscheckChildren = Maps.difference(sourceChildren, targetChildren);

        OrderResponseDto.OrderResponseDtoBuilder orderResponseBuilder = OrderResponseDto.builder();
        if (crosscheckRoot.areEqual()) {
            log.info("The trees with id: [{}] have same root elements and are eligible for merging", orderRequestSourceDto.getId());
            OrderRequestDto orderRequestDto = crosscheckRoot.entriesInCommon().values().stream().findFirst().get();
            orderResponseBuilder.identifier(orderRequestDto.getId()).brickId(orderRequestDto.getType()).operation(Operation.NO_ACTION);
            mergeChildrenAndAttributes(crosscheckAttributes, crosscheckChildren, orderResponseBuilder);
        } else if (idsAreSame(crosscheckRoot)) {
            OrderRequestDto orderRequestDto = crosscheckRoot.entriesDiffering().values().stream().findFirst().get().rightValue();
            orderResponseBuilder.identifier(orderRequestDto.getId()).brickId(orderRequestDto.getType()).operation(Operation.UPDATE);
            mergeChildrenAndAttributes(crosscheckAttributes, crosscheckChildren, orderResponseBuilder);
        } else {
            log.info("Unable to merge trees with different id elements: initial_tree: [{}] and new_tree: [{}]", orderRequestSourceDto.getId(), orderRequestTargetDto.getId());
        }
        return orderResponseBuilder.build();
    }

    /**
     * Method that deserialize json file into a {@link OrderRequestDto}
     *
     * @param requestOrderFilePath
     * @return
     * @throws IOException
     */
    public static OrderRequestDto parseRequestOrderFile(String requestOrderFilePath) throws IOException {
        InputStream resourceAsStream = OrderTreeTransformer.class.getClassLoader().getResourceAsStream(requestOrderFilePath);
        JsonReader reader = new JsonReader(new InputStreamReader(resourceAsStream, UTF_8));
        OrderRequestDto orderRequestDto = gson.fromJson(reader, OrderRequestDto.class);
        reader.close();
        return orderRequestDto;
    }

    /**
     * Method that deserialize json file into a {@link OrderResponseDto}
     *
     * @param newTreePath
     * @return
     * @throws IOException
     */
    public static OrderResponseDto parseResponseOrderFile(String newTreePath) throws IOException {
        InputStream resourceAsStream = OrderTreeTransformer.class.getClassLoader().getResourceAsStream(newTreePath);
        JsonReader reader = new JsonReader(new InputStreamReader(resourceAsStream, UTF_8));
        OrderResponseDto tcspResponseDto = gson.fromJson(reader, OrderResponseDto.class);
        reader.close();
        return tcspResponseDto;
    }

    private static boolean idsAreSame(MapDifference<String, OrderRequestDto> crosscheckRoot) {
        if (crosscheckRoot.entriesDiffering().values().stream().findFirst().isPresent() && crosscheckRoot.entriesDiffering().values().stream().findFirst().isPresent()) {
            return crosscheckRoot.entriesDiffering().values().stream().findFirst().get().leftValue().getId()
                    .equals(crosscheckRoot.entriesDiffering().values().stream().findFirst().get().rightValue().getId());
        } else return false;
    }


    private static void mergeChildrenAndAttributes(MapDifference<String, AttributeDto> crosscheckAttributes, MapDifference<String, ChildDto> crosscheckChildren, OrderResponseDto.OrderResponseDtoBuilder orderResponseBuilder) {
        List<AttributeDto> attributeDtoList = AttributesTransformer.compareAndMergeAttributes(crosscheckAttributes);
        orderResponseBuilder.attributes(attributeDtoList);
        if (crosscheckChildren.areEqual()) {
            crosscheckChildren.entriesInCommon().values().stream().forEach(a -> a.setOperation(Operation.NO_ACTION));
            orderResponseBuilder.children(crosscheckChildren.entriesInCommon().values());
        } else {
            orderResponseBuilder.children(ChildTransfromer.mergeChildren(crosscheckChildren));
        }
    }


}
