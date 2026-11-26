package com.site.customer.dto;

/**
 * Shape of an order as returned by the Orders microservice.
 */
public record OrderDto(Long id, String product, int quantity, double price, Long customerId) {
}
