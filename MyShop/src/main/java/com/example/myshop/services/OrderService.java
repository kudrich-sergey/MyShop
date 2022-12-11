package com.example.myshop.services;

import com.example.myshop.models.Cart;
import com.example.myshop.models.Order;
import com.example.myshop.models.Product;
import com.example.myshop.models.User;
import com.example.myshop.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getUserById(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrdersOrderByDesk() {
        return orderRepository.findByAllOrdersOrderByDesk();
    }

    public List<Order> getAllOrdersUserOrderByDesk(int id_user) {
        return orderRepository.findByAllOrdersUserOrderByDesk(id_user);
    }

    public List<Order> getOrdersByNumber(String number) {
        return orderRepository.findByOrder(number);
    }

    public Order getOrderId(int id) {
        Optional<Order> order_bd = orderRepository.findById(id);
        return order_bd.orElse(null);
    }

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public void updateStatusOrder(int id_status, int id_order) {
        orderRepository.updateRoleUser(id_status, id_order);
    }

}
