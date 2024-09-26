package com.user_manager.User_Management.rest_controllers;

import com.user_manager.User_Management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = {"*"},
        maxAge = 3600L
)
@RestController
@RequestMapping("/api/v1")
public class RegisterApiController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestParam("fullname") String fullname,
                                          @RequestParam("username") String username,
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password){

        System.out.println(String.format("Received request to register user %s", fullname));

        if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Please complete all fields", HttpStatus.BAD_REQUEST);
        }

        // Hash the password

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hashedPassword);

        //Register new user
        //int result = userService.registerNewUserServiceMethod(fullname, username, email, hashedPassword);
        int result = userService.registerNewUserServiceMethod(fullname, username, email, hashedPassword);

        if(result != 1){
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
        }

            return new ResponseEntity<>("success", HttpStatus.OK);


    }
}
