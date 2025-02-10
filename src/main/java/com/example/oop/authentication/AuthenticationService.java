package com.example.oop.authentication;

import com.example.oop.models.User;
import com.example.oop.exceptions.AuthenticationException;

public interface AuthenticationService {
    User login(String email, String password) throws AuthenticationException;
    void register(User user);
}
