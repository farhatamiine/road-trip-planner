package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Entities.User;

import java.util.List;

// (user management, authentication)
public interface UserService {
    User findUserByEmail(String email);

    User findUserByUsername(String username);

    User findUserById(int id);

    List<User> findAllUsers();

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}
