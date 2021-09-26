package com.baml.OrderEngine.rest.mapper;

import com.baml.OrderEngine.exception.OrderCreationException;
import com.baml.OrderEngine.model.order.BuyOrder;
import com.baml.OrderEngine.model.order.Order;
import com.baml.OrderEngine.model.order.SellOrder;
import com.baml.OrderEngine.model.side.Side;
import com.baml.OrderEngine.rest.dto.OrderDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.baml.OrderEngine.model.util.OrderBookUtil.UNKNOWN_ORDER_SIDE;

@Component
public class OrderDtoMapper {

    public Order mapOrderDto(int currentCounterId, OrderDto orderDto) throws OrderCreationException {
        switch (orderDto.getSide()) {
            case BUY:
                return new BuyOrder(currentCounterId, orderDto);
            case SELL:
                return new SellOrder(currentCounterId, orderDto);
            default:
                throw new OrderCreationException(UNKNOWN_ORDER_SIDE);
        }
    }

    //"buy 250 shares at 20.35"
    public OrderDto mapInstructions(String instructions) throws OrderCreationException {
        String[] split = instructions.split("\\s");
        validateInstructions(split);
        Side side = parseSide(split[0]);
        Integer quantity = parseQuantity(split[1]);
        BigDecimal price = parsePrice(split[4]);
        return new OrderDto(price, side, quantity, LocalDateTime.now());
    }

    private Side parseSide(String sideString) throws OrderCreationException {
        Side side;
        try {
            side = Side.valueOf(sideString.toUpperCase());
        } catch (IllegalArgumentException e) {
            String message = String.format("Invalid instruction side %s - expected BUY or SELL", sideString);
            throw new OrderCreationException(message);
        }
        return side;
    }

    private Integer parseQuantity(String quantityString) throws OrderCreationException {
        try {
            return Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            String message = String.format("Invalid instruction quantity %s - expected valid integer", quantityString);
            throw new OrderCreationException(message);
        }
    }

    private BigDecimal parsePrice(String priceString) throws OrderCreationException {
        try {
            return new BigDecimal(priceString);
        } catch (NumberFormatException e) {
            String message = String.format("Invalid instruction price %s - expected valid number", priceString);
            throw new OrderCreationException(message);
        }
    }

    private void validateInstructions(String[] split) throws OrderCreationException {
        if (split.length != 5) {
            String message = String.format("Invalid length of instruction %s - expected 5", split.length);
            throw new OrderCreationException(message);
        }
        String shares = split[2];
        String at = split[3];
        validateWordings(split, shares, at);
    }

    private void validateWordings(String[] split, String shares, String at) throws OrderCreationException {
        if (!"shares".equalsIgnoreCase(shares) || !"at".equalsIgnoreCase(at)) {
            String message = String.format("Invalid instruction wordings %s %s - expected \"shares at\"", split[2], split[3]);
            throw new OrderCreationException(message);
        }
    }
}
