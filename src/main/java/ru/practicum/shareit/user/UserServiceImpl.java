package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public UserDto saveUser(UserDto user) {
        return repository.save(user);
    }

    @Override
    public UserDto updateUser(Long id,UserDto user){
        return repository.update(id,user);
    }

    @Override
    public UserDto getUserById(Long id){
        return repository.getUserById(id);
    }

    @Override
    public void deleteUser(Long id){ repository.deleteUser(id);}
}