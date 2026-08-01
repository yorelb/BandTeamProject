package uk.ac.sheffield.team28.team28.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.model.Order;
import uk.ac.sheffield.team28.team28.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController (OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/changeOrderStatus")
    public String changeOrderStatus(@RequestParam("orderId") Long orderId) {
        try {
            orderService.changeOrderStatus(orderId);
            return "redirect:/committee/dashboard";
        } catch (Exception e) {
            return "error";
        }
    }
}
