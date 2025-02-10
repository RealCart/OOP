package com.example.oop.authentication;
import com.example.oop.exceptions.AuthenticationException;
import com.example.oop.models.User;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final Map<String, User>  users = new HashMap<>();

    @Override
    public User login(String email, String password) throws AuthenticationException {
        User user = users.get(email);
        if(user == null) {
            throw new AuthenticationException("Пользователь с таким email не найден.");
        }
        if(!user.getPassword().equals(password)) {
            throw new AuthenticationException("Неверный пароль.");
        }
        return user;
    }

    @Override
    public void register(User user) {
        users.put(user.getEmail(), user);
    }
}
