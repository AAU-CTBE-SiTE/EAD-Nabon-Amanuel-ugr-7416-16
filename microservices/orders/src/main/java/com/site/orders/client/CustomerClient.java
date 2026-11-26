package com.site.orders.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Calls the Customer microservice using RestClient.
 */
@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(RestClient customerRestClient) {
        this.restClient = customerRestClient;
    }

    // GET http://localhost:8081/api/customers/{id}
    // true if the customer exists (200), false if it does not (404)
    public boolean customerExists(Long customerId) {
        try {
            restClient.get()
                    .uri("/api/customers/{id}", customerId)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Customer service is not reachable: " + e.getMessage());
        }
    }
}
