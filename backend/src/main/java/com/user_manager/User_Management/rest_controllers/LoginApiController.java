package com.user_manager.User_Management.rest_controllers;

import com.user_manager.User_Management.models.Login;
import com.user_manager.User_Management.models.User;
import com.user_manager.User_Management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LoginApiController {

    @Autowired
    UserService userService;


    @PostMapping("/user/login")
    public ResponseEntity authenticateUser(@RequestBody Login login){

        List<String> userEmail = userService.checkUserEmail(login.getEmail());

        if(userEmail.isEmpty() || userEmail == null){
            return new ResponseEntity("Email does not exist", HttpStatus.NOT_FOUND);
        }

        // Get hashed pword
        String hashedPwd = userService.checkUserPasswordByEmail(login.getEmail());

        if(!BCrypt.checkpw(login.getPassword(), hashedPwd)){
            return new ResponseEntity("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUserDetailsByEmail(login.getEmail());
        return new ResponseEntity(user, HttpStatus.OK);
    }
}
