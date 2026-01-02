package ch.martinelli.demo.nested.repository;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderDTO(Long id, LocalDateTime orderDate,
                        CustomerDTO customer, List<OrderItemDTO> items) {
}