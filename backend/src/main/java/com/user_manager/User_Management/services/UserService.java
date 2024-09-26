package com.user_manager.User_Management.services;

import com.user_manager.User_Management.models.User;
import com.user_manager.User_Management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public int registerNewUserServiceMethod(String fname, String uname, String email, String password){
        return userRepository.registerNewUser(fname, uname, email, password);
    }

    //Check user email service method
    public List<String> checkUserEmail(String email){
        return userRepository.checkUserEmail(email);
    }

    //End of check user email service

    public String checkUserPasswordByEmail(String email){
        return userRepository.checkUserPassword(email);
    }

    public User getUserDetailsByEmail(String email){
        return userRepository.findUserByEmail(email);
    }
}
