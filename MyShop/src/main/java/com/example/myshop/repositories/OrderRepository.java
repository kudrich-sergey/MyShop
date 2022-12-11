package com.example.myshop.repositories;

import com.example.myshop.models.Order;
import com.example.myshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);

    @Query(value = "select * from orders where (lower(number) LIKE %?1)", nativeQuery = true)
    List<Order> findByOrder(String search);

    @Query(value = "select * from orders order by id desc", nativeQuery = true)
    List<Order> findByAllOrdersOrderByDesk();

    @Modifying
    @Query(value = "update orders set status=?1 where id=?2", nativeQuery = true)
    void updateRoleUser(int id_status, int id_order);

    @Query(value = "select * from orders where user_id=?1 order by id desc", nativeQuery = true)
    List<Order> findByAllOrdersUserOrderByDesk(int id_user);

}
