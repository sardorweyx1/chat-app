package uz.weyx.chatapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.weyx.chatapp.entity.Chat;
import uz.weyx.chatapp.entity.User;
import uz.weyx.chatapp.service.UserService;
import uz.weyx.chatapp.service.payload.UserDto;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<User> userList = userService.getAll();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/chats")
    public ResponseEntity<?> getChatsById(@PathVariable Long id) {
        Set<Chat> chatList = userService.getChatsByUserId(id);
        return ResponseEntity.ok(chatList);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDto userDto) {
        User user = userService.save(userDto);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id,
                                  @RequestBody UserDto userDto) {
        User user = userService.edit(id, userDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean success = userService.delete(id);
        return ResponseEntity.ok(success);
    }

}
