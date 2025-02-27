package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    /**
     * @param email
     * @return
     */
    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    /**
     * @param username
     * @return User
     */
    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public User findUserById(int id) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    /**
     * @param user
     */
    @Override
    public void saveUser(User user) {

    }

    /**
     * @param user
     */
    @Override
    public void updateUser(User user) {

    }

    /**
     * @param id
     */
    @Override
    public void deleteUser(int id) {

    }
}
