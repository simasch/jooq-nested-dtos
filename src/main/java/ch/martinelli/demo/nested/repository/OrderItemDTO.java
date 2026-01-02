package ch.martinelli.demo.nested.repository;

public record OrderItemDTO(Long id, int quantity, ProductDTO product) {
}