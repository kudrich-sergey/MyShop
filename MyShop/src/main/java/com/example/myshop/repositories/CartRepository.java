package com.example.myshop.repositories;

import com.example.myshop.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findByUserId(int id);

    @Modifying
    @Query(value = "delete from product_cart where product_id=?1 and user_id=?2", nativeQuery = true)
    void deleteProductFromCartById(int id, int user_id);

}
