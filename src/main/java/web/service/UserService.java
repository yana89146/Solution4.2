package web.service;
import web.model.Role;
import web.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {


    void add(User user);

    List<User> getAllUsers();

    void deleteById(Long id);

    User findById(Long id);

    void updateById(User user, Long id);

    User findByUsername(String firstname);
}
