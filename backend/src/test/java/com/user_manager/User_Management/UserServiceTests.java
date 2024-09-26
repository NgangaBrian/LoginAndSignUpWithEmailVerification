package com.user_manager.User_Management;

import com.user_manager.User_Management.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void  testRegisterNewUser(){

        String password = "pword123";
        String hashedpwd = BCrypt.hashpw(password, BCrypt.gensalt());
        int result = userService.registerNewUserServiceMethod("John Doe", "johndoe","jd@gmail.com", hashedpwd);
        assertEquals(1, result);
    }
}
