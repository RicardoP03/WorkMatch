package ro.unibuc.hello.service;

import ro.unibuc.hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.exception.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userEntity -> new User(userEntity.getId(),userEntity.getName(), userEntity.getRole(), userEntity.getPassword()))
                .collect(Collectors.toList());
    }
    

    public User getUserById(String id) throws EntityNotFoundException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return new User(user.getId(), user.getName(), user.getRole(), user.getPassword());
    }

    public User saveUser(User user) {
        UserEntity userEntity = new UserEntity( user.getName(), user.getRole(), user.getPassword());
        userEntity = userRepository.save(userEntity);
        return new User(userEntity.getId(), userEntity.getName(), userEntity.getRole(), userEntity.getPassword());
    }
    
    
    public User updateUser(String id, User user) throws EntityNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        userEntity.setName(user.getName());
        userEntity.setRole(user.getRole());
        userEntity.setPassword(user.getPassword());
        userRepository.save(userEntity);
        return new User(userEntity.getId(), userEntity.getName(), userEntity.getRole(), userEntity.getPassword());
    }

    public void deleteUser(String id) throws EntityNotFoundException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        userRepository.delete(user);
    }

    public User getUserByName(String name) throws EntityNotFoundException {
        UserEntity user = userRepository.findByName(name);
        if (user == null) {
            throw new EntityNotFoundException("User not found with name: " + name);
        }
        return new User(user.getId(), user.getName(), user.getRole(), user.getPassword());
    }
}
