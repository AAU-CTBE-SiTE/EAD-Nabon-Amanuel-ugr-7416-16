package com.site.customer.client;

import com.site.customer.dto.OrderDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Calls the Orders microservice using RestClient.
 */
@Component
public class OrderClient {

    private final RestClient restClient;

    public OrderClient(RestClient ordersRestClient) {
        this.restClient = ordersRestClient;
    }

    // GET http://localhost:8082/api/orders/customer/{customerId}
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        try {
            List<OrderDto> orders = restClient.get()
                    .uri("/api/orders/customer/{customerId}", customerId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<OrderDto>>() {
                    });
            return orders == null ? List.of() : orders;
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Orders service is not reachable: " + e.getMessage());
        }
    }
}
