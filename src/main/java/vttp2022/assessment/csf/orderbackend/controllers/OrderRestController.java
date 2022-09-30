package vttp2022.assessment.csf.orderbackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    @Autowired
    private OrderService orderSvc;

    @PostMapping()
    public ResponseEntity<String> newOrder(@RequestBody String payload) {

        //System.out.println(">>>>>> order: " + payload);
        Order order = Order.createOrder(payload);

        orderSvc.createOrder(order);

        return ResponseEntity.ok("{}");
    }

    @GetMapping("/{email}/all")
    public List<OrderSummary> getOrdersList(@PathVariable String email) {

        List<OrderSummary> listOrders = orderSvc.getOrdersByEmail(email);

        return listOrders;
    }

}
