package uz.weyx.chatapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.weyx.chatapp.entity.Chat;
import uz.weyx.chatapp.service.ChatService;
import uz.weyx.chatapp.service.payload.ChatDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Chat> chatList = chatService.getAll();
        return ResponseEntity.ok(chatList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Chat chat = chatService.getById(id);
        return ResponseEntity.ok(chat);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ChatDto chatDto) {
        Chat chat = chatService.save(chatDto);
        return ResponseEntity.ok(chat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id, @RequestBody ChatDto chatDto) {
        Chat newChat = chatService.edit(id, chatDto);
        return ResponseEntity.ok(newChat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        String success = chatService.delete(id);
        return ResponseEntity.ok(success);
    }
}
