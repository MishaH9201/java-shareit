package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private static long lastId=0;
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        if(users.containsValue(user)){
            throw new ResponseStatusException(HttpStatus.CONFLICT ,"User already exists");
        }
        checkRepeatEmail(user);
       /* if(users.values().stream().
                map(UserDto::getEmail).
                anyMatch(user.getEmail()::equals)){
            throw new ResponseStatusException(HttpStatus.CONFLICT ,"Email already in use");
        }*/
        if(user.getId() == null){
        user.setId(getId());}
        users.put(user.getId(),user);
        return user;
    }
   @Override
    public User update(Long id,User user){
        if(users.containsKey(id)){
            User userUpdate =users.get(id);
            if(user.getEmail()!=null){
                checkRepeatEmail(user);
                userUpdate.setEmail(user.getEmail());
            }
            if(user.getName()!=null){
                userUpdate.setName(user.getName());
            }
            users.put(id, userUpdate);
            return userUpdate;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist");
        }
    }

    @Override
    public User getUserById(Long id){
        if(users.containsKey(id)){
            return users.get(id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist");
        }
    }
    @Override
    public void deleteUser(Long id){
        if(users.containsKey(id)){
             users.remove(id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist");
        }
    }
    private long getId() {
       /* long lastId = users.values().stream()
                .mapToLong(UserDto::getId)
                .max()
                .orElse(0);*/
         lastId++;
        return lastId;
    }
    private void checkRepeatEmail(User user){
        if(user.getEmail()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"No email");
        }
        if(users.values().stream().
                map(User::getEmail).
                anyMatch(user.getEmail()::equals)){
            throw new ResponseStatusException(HttpStatus.CONFLICT ,"Email already in use");
        }
    }
}
