package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public User updateUser(Long id,User user){
        return repository.update(id,user);
    }

    @Override
    public User getUserById(Long id){
        return repository.getUserById(id);
    }

    @Override
    public void deleteUser(Long id){ repository.deleteUser(id);}
}