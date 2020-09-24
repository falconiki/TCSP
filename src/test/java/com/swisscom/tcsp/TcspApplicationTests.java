package com.swisscom.tcsp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.tcsp.dto.Operation;
import com.swisscom.tcsp.dto.ChildDto;
import com.swisscom.tcsp.dto.OrderResponseDto;
import com.swisscom.tcsp.transformer.OrderTreeTransformer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TcspApplicationTests extends WebLayerTest {

    private static final String ID_1 = "id-1";
    private static final String ID_2 = "id-2";
    private static final String ID_3 = "id-3";
    private static final String ONLY_IN_INITIAL = "ONLY_IN_INITIAL";
    private static final String IN_BOTH_TREES = "IN_BOTH_TREES";
    private static final String ONLY_IN_NEW = "ONLY_IN_NEW";
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testOrderSameRootsSameAttributeDifferentChildren() throws Exception {
        //arrange
        String folder = "same-attribute-diff-children";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        //act
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        //assert
        assertDifferentChildren(actualChildren);
        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size(), actualChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size());
        assertEquals(expectedOrderDto.getAttributes().size(), actualOrderDto.getAttributes().size());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }

    @Test
    public void testOrderNoCommonChildren() throws Exception {
        //arrange
        String folder = "no-common-children";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        //act
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        //assert
        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size(), actualChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size());
        assertEquals(expectedOrderDto.getAttributes().size(), actualOrderDto.getAttributes().size());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }

    @Test
    public void testOrderSameRootsDiffAttributeSameChildren() throws Exception {
        //arrange
        String folder = "diff-attribute-same-children";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        //act
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        //assert
        assertNoChangeChildren(actualChildren, expectedChildren);
        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size(), actualChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size());
        assertEquals(expectedOrderDto.getAttributes().size(), actualOrderDto.getAttributes().size());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }

    @Test
    public void testOrderSameRootsDiffAttributeDifferentChildren() throws Exception {
        //arrange
        String folder = "diff-attribute-diff-children";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        //act
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        //assert
        assertDifferentChildren(actualChildren);
        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size(), actualChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size());
        assertEquals(expectedOrderDto.getAttributes().size(), actualOrderDto.getAttributes().size());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }

    @Test
    public void testCreateOrderSameTrees() throws Exception {
        //arrange
        String folder = "same-trees";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        //act
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        //assert
        assertNoChangeChildren(actualChildren, expectedChildren);
        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size(), actualChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size());
        assertEquals(expectedOrderDto.getAttributes().size(), actualOrderDto.getAttributes().size());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }

    @Test
    public void testCreateOrderSameTreesUpdatedChildren() throws Exception {
        //arrange
        String folder = "same-trees-updated-children";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        //act
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        ChildDto updatedChild = actualChildren.stream().filter(m -> m.getOperation().equals(Operation.UPDATE)).findFirst().get();
        //assert
        assertEquals(2, actualChildren.stream().count());
        assertEquals(1, actualChildren.stream().filter(m -> m.getOperation().equals(Operation.NO_ACTION)).count());
        assertEquals(1, actualChildren.stream().filter(m -> m.getOperation().equals(Operation.UPDATE)).count());
        assertEquals(ID_2, updatedChild.getId());
        assertEquals(3, updatedChild.getAttributes().size());

        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size(), actualChildren.stream().map(ChildDto::getAttributes).collect(Collectors.toList()).size());
        assertEquals(expectedOrderDto.getAttributes().size(), actualOrderDto.getAttributes().size());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }

    @Test
    public void testCreateOrderLargeTrees() throws Exception {
        //arrange
        String folder = "large-trees";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        //act
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        //assert
        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedOrderDto.getAttributes().size(), actualOrderDto.getAttributes().size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).count(), actualChildren.stream().map(ChildDto::getAttributes).count());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }

    @Test
    public void testNoChildren() throws Exception {
        //arrange
        String folder = "no-children";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        //act
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        //assert
        assertEquals(expectedOrderDto.getBrickId(), actualOrderDto.getBrickId());
        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedOrderDto.getAttributes().size(), actualOrderDto.getAttributes().size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).count(), actualChildren.stream().map(ChildDto::getAttributes).count());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }


    @Test
    public void testNoAttributes() throws Exception {
        //arrange
        String folder = "no-attributes";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        //act
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        //assert
        assertEquals(expectedOrderDto.getBrickId(), actualOrderDto.getBrickId());
        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedOrderDto.getAttributes().size(), actualOrderDto.getAttributes().size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).count(), actualChildren.stream().map(ChildDto::getAttributes).count());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }


    @Test
    public void testSameIdDifferentType() throws Exception {
        //arrange
        String folder = "same-id-diff-type";
        OrderResponseDto expectedOrderDto = parseExpectedResultOrder(folder);
        ResultActions order = createOrder(folder);
        String actualResult = order.andReturn().getResponse().getContentAsString();
        OrderResponseDto actualOrderDto = objectMapper.readValue(actualResult, OrderResponseDto.class);
        //act
        Collection<ChildDto> expectedChildren = expectedOrderDto.getChildren();
        Collection<ChildDto> actualChildren = actualOrderDto.getChildren();
        //assert
        assertEquals(expectedOrderDto.getBrickId(), actualOrderDto.getBrickId());
        assertEquals(expectedOrderDto.getOperation(), actualOrderDto.getOperation());
        assertEquals(expectedChildren.size(), actualChildren.size());
        assertEquals(expectedChildren.stream().map(ChildDto::getAttributes).count(), actualChildren.stream().map(ChildDto::getAttributes).count());
        assertTrue(expectedOrderDto.getAttributes().containsAll(actualOrderDto.getAttributes()));
    }

    private ResultActions createOrder(String folder) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(Constants.Mapping.BASE_URL + Constants.TcspEventConstants.TCSP + "/?folder=" + folder)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private void assertNoChangeChildren(Collection<ChildDto> actualChildren, Collection<ChildDto> expectedChildren) {
        List<ChildDto> noActionChildren = actualChildren.stream().filter(m -> m.getOperation().equals(Operation.NO_ACTION)).collect(Collectors.toList());
        assertEquals(2, actualChildren.stream().count());
        assertEquals(2, noActionChildren.size());
        assertEquals(1, noActionChildren.stream().filter(m -> m.getId().equals(ID_1)).count());
        assertEquals(1, noActionChildren.stream().filter(m -> m.getId().equals(ID_2)).count());
        assertEquals(2, noActionChildren.stream().filter(m -> m.getType().equals("IN_BOTH_TREES")).count());
        assertNotNull(noActionChildren.stream().filter(m -> m.getId().equals(ID_1)).findFirst().get().getRelations());
    }

    private void assertDifferentChildren(Collection<ChildDto> actualChildren) {
        ChildDto childCreated = getChild(actualChildren, Operation.CREATE);
        ChildDto childUpdated = getChild(actualChildren, Operation.UPDATE);
        ChildDto childDeleted = getChild(actualChildren, Operation.DELETE);

        assertEquals(ID_1, childDeleted.getId());
        assertEquals(ID_2, childUpdated.getId());
        assertEquals(ID_3, childCreated.getId());

        assertEquals(ONLY_IN_INITIAL, childDeleted.getType());
        assertEquals(IN_BOTH_TREES, childUpdated.getType());
        assertEquals(ONLY_IN_NEW, childCreated.getType());
        assertNotNull(childCreated.getRelations());
        assertNotNull(childDeleted.getRelations());
        assertNull(childUpdated.getRelations());
    }

    private OrderResponseDto parseExpectedResultOrder(String folder) throws IOException {
        return OrderTreeTransformer.parseResponseOrderFile(folder + Constants.JsonFileUtilConstants.EXPECTED_RESULT);
    }

    private ChildDto getChild(Collection<ChildDto> actualChildren, Operation create) {
        return actualChildren.stream().filter(m -> m.getOperation().equals(create)).findFirst().get();
    }

    public static String readExpectedResultFileAsString(String folder) throws Exception {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/" + folder + "/expected_result.json")));
    }

}
