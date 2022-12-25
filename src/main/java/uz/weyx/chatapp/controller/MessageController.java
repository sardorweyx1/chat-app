package uz.weyx.chatapp.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.weyx.chatapp.entity.Message;
import uz.weyx.chatapp.service.MessageService;
import uz.weyx.chatapp.service.payload.UrlDto;
import uz.weyx.chatapp.service.payload.MessageDto;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<Message> messageList = messageService.getAll();
        return ResponseEntity.ok(messageList);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllByUserId(@PathVariable Long id) {
        List<Message> messageList = messageService.getAllByUserId(id);
        return ResponseEntity.ok(messageList);
    }

    @GetMapping("/chat/{id}")
    public ResponseEntity<?> getByChatId(@PathVariable Long id) {
        List<UrlDto> messageList = messageService.getByChatId(id);
        return ResponseEntity.ok(messageList);
    }

    @PostMapping("/upload/photo")
    public ResponseEntity<?> save(@RequestBody MessageDto messageDto) throws IOException {
        String success = messageService.save(messageDto);
        return ResponseEntity.ok(success);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id,
                                  @RequestBody MessageDto messageDto) throws IOException {
        String message = messageService.edit(id, messageDto);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        String success = messageService.detele(id);
        return ResponseEntity.ok(success);
    }
}
