package ch.martinelli.demo.nested.repository;

public record CustomerDTO(Long id, String firstName, String lastName, String street, String postalCode, String city) {
}
