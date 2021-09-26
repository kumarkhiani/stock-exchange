package com.baml.OrderEngine.rest.mapper;

import com.baml.OrderEngine.exception.OrderCreationException;
import com.baml.OrderEngine.model.side.Side;
import com.baml.OrderEngine.rest.dto.OrderDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderDtoMapperTest {

    private final OrderDtoMapper mapper = new OrderDtoMapper();

    @Test
    void testValidInstruction() throws OrderCreationException {
        String instruction = "buy 250 shares at 20.35";
        OrderDto orderDto = mapper.mapInstructions(instruction);
        assertThat(orderDto.getSide(), is(equalTo(Side.BUY)));
        assertThat(orderDto.getQuantity(), is(250));
        assertThat(orderDto.getPrice(), is(equalTo(BigDecimal.valueOf(20.35))));
    }

    @Test
    void testInvalidInstructionSide() {
        String instruction = "xxx 250 shares at 20.35";
        Exception e = assertThrows(OrderCreationException.class, () -> {
            mapper.mapInstructions(instruction);
        });
        assertEquals(e.getMessage(), "Invalid instruction side xxx - expected BUY or SELL");
    }

    @Test
    void testInvalidInstructionQuantity() {
        String instruction = "buy xxx shares at 20.35";
        Exception e = assertThrows(OrderCreationException.class, () -> {
            mapper.mapInstructions(instruction);
        });
        assertEquals(e.getMessage(), "Invalid instruction quantity xxx - expected valid integer");
    }

    @Test
    void testInvalidInstructionPrice() {
        String instruction = "buy 250 shares at xxx";
        Exception e = assertThrows(OrderCreationException.class, () -> {
            mapper.mapInstructions(instruction);
        });
        assertEquals(e.getMessage(), "Invalid instruction price xxx - expected valid number");
    }

    @Test
    void testInvalidInstructionLength() {
        String instruction = "buy 250 shares 20.35";
        Exception e = assertThrows(OrderCreationException.class, () -> {
            mapper.mapInstructions(instruction);
        });
        assertEquals(e.getMessage(), "Invalid length of instruction 4 - expected 5");
    }
}