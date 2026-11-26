package com.site.orders.service;

import com.site.orders.client.CustomerClient;
import com.site.orders.model.Order;
import com.site.orders.repo.OrderRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final CustomerClient customerClient;

    public OrderService(OrderRepo orderRepo, CustomerClient customerClient) {
        this.orderRepo = orderRepo;
        this.customerClient = customerClient;
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found with id " + id));
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepo.findByCustomerId(customerId);
    }

    public Order createOrder(Order order) {
        validateCustomer(order.getCustomerId());
        order.setId(null); // make sure a NEW row is inserted
        return orderRepo.save(order);
    }

    public Order updateOrder(Long id, Order updated) {
        Order existing = getOrderById(id);
        validateCustomer(updated.getCustomerId());
        existing.setProduct(updated.getProduct());
        existing.setQuantity(updated.getQuantity());
        existing.setPrice(updated.getPrice());
        existing.setCustomerId(updated.getCustomerId());
        return orderRepo.save(existing);
    }

    public void deleteOrder(Long id) {
        if (!orderRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Order not found with id " + id);
        }
        orderRepo.deleteById(id);
    }

    // Asks the Customer microservice whether the customer exists
    private void validateCustomer(Long customerId) {
        if (!customerClient.customerExists(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Customer not found with id " + customerId);
        }
    }
}
