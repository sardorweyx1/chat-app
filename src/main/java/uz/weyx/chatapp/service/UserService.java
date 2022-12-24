package uz.weyx.chatapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.weyx.chatapp.entity.Chat;
import uz.weyx.chatapp.entity.User;
import uz.weyx.chatapp.repository.UserRepository;
import uz.weyx.chatapp.service.payload.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() -> new IllegalArgumentException("User not Found"));
    }


    public User save(UserDto userDto) {
        boolean username = userRepository.existsByUsername(userDto.getUsername());
        if (username) return new User();
        User user = new User();
        user.setUsername(userDto.getUsername());
        userRepository.save(user);
        return user;
    }

    public User edit(Long id, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return new User();
        }
        User user = optionalUser.get();
        user.setUsername(userDto.getUsername());
        return userRepository.save(user);
    }

    public boolean delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return false;
        userRepository.deleteById(id);
        return true;
    }

    public Set<Chat> getChatsByUserId(Long userId) {
        User user = getById(userId);
        return user.getChats();
    }

    public Set<User> getUsersIn(Set<Long> userId) {
        return userRepository.findAllByIdIn(userId);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
