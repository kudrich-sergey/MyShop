package com.example.myshop.repositories;

import com.example.myshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    @Query(value = "select * from users order by id desc", nativeQuery = true)
    List<User> findByAllUserOrderByDesk();

    @Modifying
    @Query(value = "update users set role=?1 where id=?2", nativeQuery = true)
    void updateRoleUser(String user_role, int id_user);


}
