package authentication;

import models.User;
import exceptions.AuthenticationException;

public interface AuthenticationService {
    User login(String email, String password) throws AuthenticationException;
    void register(User user);
}
