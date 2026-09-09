package com.example.aiSpring.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderTools {
    private static final Map<String, String> ORDERS = Map.of(
            "1001", "Shipped - dispatched on 2026-09-05, tracking XY123, arriving 2026-09-11",
            "1002", "Pending payment - awaiting card authorization, placed 2026-09-07",
            "1003", "Cancelled - order cancelled by customer 2026-09-08, refund issued"
    );

    @Tool(description = "Look up the current status of a single customer order by its order ID")
    public String getOrderStatus(
            @ToolParam(description = "The order ID to look up") String orderId) {
        System.out.println(">>> TOOL CALLED: getOrderStatus for orderId: " + orderId);
        return ORDERS.getOrDefault(orderId, "No order found with ID: " + orderId);
    }

    @Tool(description = "List all orders that are currently open and not yet delivered")
    public String listOpenOrders() {
        System.out.println(">>> TOOL CALLED: listOpenOrders");
        return "1002 - PENDING PAYMENT; 1004 - AWAITING STOCK; 1005 - IN PACKING";
    }

}
