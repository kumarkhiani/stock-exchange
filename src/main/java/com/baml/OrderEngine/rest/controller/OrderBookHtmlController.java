package com.baml.OrderEngine.rest.controller;

import com.baml.OrderEngine.service.OrderBookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderBookHtmlController {

    OrderBookService service;

    public OrderBookHtmlController(OrderBookService service) {
        this.service = service;
    }

    @GetMapping("/getOrderBook")
    public String getOrderBook(Model model) {
        model.addAttribute("orders", service.getSortedOrdersForDisplay());
        model.addAttribute("trades", service.getTradeHistory());
        return "order-book";
    }
}
