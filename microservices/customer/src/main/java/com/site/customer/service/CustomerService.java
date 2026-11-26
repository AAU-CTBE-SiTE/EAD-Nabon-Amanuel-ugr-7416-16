package com.site.customer.service;

import com.site.customer.client.OrderClient;
import com.site.customer.dto.OrderDto;
import com.site.customer.model.Customer;
import com.site.customer.repo.CustomerRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;
    private final OrderClient orderClient;

    public CustomerService(CustomerRepo customerRepo, OrderClient orderClient) {
        this.customerRepo = customerRepo;
        this.orderClient = orderClient;
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found with id " + id));
    }

    public Customer createCustomer(Customer customer) {
        customer.setId(null); // make sure a NEW row is inserted
        return customerRepo.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = getCustomerById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        return customerRepo.save(existing);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer not found with id " + id);
        }
        customerRepo.deleteById(id);
    }

    // Finds all orders of one customer by calling the Orders microservice
    public List<OrderDto> getOrdersForCustomer(Long customerId) {
        getCustomerById(customerId); // 404 if the customer does not exist
        return orderClient.getOrdersByCustomerId(customerId);
    }
}
