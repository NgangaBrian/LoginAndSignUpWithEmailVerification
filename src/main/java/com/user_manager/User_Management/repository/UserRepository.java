package com.user_manager.User_Management.repository;

import com.user_manager.User_Management.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "select email from users where email = :email", nativeQuery = true)
    List<String> checkUserEmail(@Param("email") String email);

    @Query(value = "Select password from users where email = :email", nativeQuery = true)
    String checkUserPassword(@Param("email") String email);

    @Query(value = "select  * from users where email = :email", nativeQuery = true)
    User findUserByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO users(fullname, username, email, password) VALUES(:fullname, :username, :email, :password)", nativeQuery = true)
    int registerNewUser(@Param("fullname") String fullname,
                        @Param("username") String username,
                        @Param("email") String email,
                        @Param("password") String password);
}
