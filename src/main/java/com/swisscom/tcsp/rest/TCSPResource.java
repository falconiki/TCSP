package com.swisscom.tcsp.rest;

import com.swisscom.tcsp.Constants;
import com.swisscom.tcsp.dto.OrderRequestDto;
import com.swisscom.tcsp.dto.OrderResponseDto;
import com.swisscom.tcsp.transformer.OrderTreeTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(Constants.Mapping.BASE_URL)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TCSPResource {

    /**
     * Endpoint that compares two trees and returns a merged result entity
     *
     * @param folder
     * @return
     * @throws IOException
     */
    @PostMapping(value = Constants.TcspEventConstants.TCSP)
    public ResponseEntity<?> compareTrees(@RequestParam(name = "folder", required = false, defaultValue = "same-attribute-diff-children") String folder) throws IOException {
        long startTime = System.currentTimeMillis();
        log.info("POST request to simulate merging the initial and new trees ");
        OrderRequestDto initialTreeDto = OrderTreeTransformer.parseRequestOrderFile(folder + Constants.JsonFileUtilConstants.INITIAL_TREE_JSON);
        OrderRequestDto newTreeDto = OrderTreeTransformer.parseRequestOrderFile(folder + Constants.JsonFileUtilConstants.NEW_TREE_JSON);
        OrderResponseDto orderResponseDto = OrderTreeTransformer.transformTreesIntoResultOrder(initialTreeDto, newTreeDto);
        log.info("Creating of the order ended with duration [{}] ms using file with name [{}]", System.currentTimeMillis() - startTime, folder);
        return ResponseEntity.ok(orderResponseDto);
    }


}
