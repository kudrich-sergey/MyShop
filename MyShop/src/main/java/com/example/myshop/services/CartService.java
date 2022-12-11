package com.example.myshop.services;

import com.example.myshop.models.Cart;
import com.example.myshop.repositories.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Transactional
    public void deleteProductFromCartById(int id, int user_id) {
        cartRepository.deleteProductFromCartById(id, user_id);
    }

    @Transactional
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public List<Cart> getUserById(int id) {
        return cartRepository.findByUserId(id);
    }

}
