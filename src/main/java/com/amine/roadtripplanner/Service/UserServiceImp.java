package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Exception.UserNotFoundException;
import com.amine.roadtripplanner.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {


    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param email
     * @return
     */
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("email", email));
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
