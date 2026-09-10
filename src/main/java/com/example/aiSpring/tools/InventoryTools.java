package com.example.aiSpring.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryTools {

    private final JdbcTemplate jdbc;

    public InventoryTools(@Qualifier("readOnlyJdbcTemplate") JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Tool(description = "Look up the current stock level of a product by its SKU code")
    public String getInventory(
            @ToolParam(description = "The product SKU code") String sku) {
        System.out.println(">>> TOOL CALLED: getInventory for SKU: " + sku);

        List<String> rows = jdbc.query(
                "SELECT name, quantity, warehouse FROM inventory WHERE sku = ?",
                (rs, i) -> rs.getString("name") + ":" +rs.getInt("quantity") +
                        " units at " + rs.getString("warehouse"),
                sku);

        return rows.isEmpty() ? "No product found with SKU: " + sku : rows.get(0);
    }
}
