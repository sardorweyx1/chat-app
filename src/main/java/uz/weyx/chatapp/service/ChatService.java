package uz.weyx.chatapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.weyx.chatapp.entity.Chat;
import uz.weyx.chatapp.entity.User;
import uz.weyx.chatapp.repository.ChatRepository;
import uz.weyx.chatapp.service.payload.ChatDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;


    public List<Chat> getAll() {
        return chatRepository.findAll();
    }

    public Chat getById(Long id) {
        Optional<Chat> optionalChat = chatRepository.findById(id);
        return optionalChat.orElseGet(Chat::new);
    }

    public Chat save(ChatDto chatDto) {
        boolean existsByName = chatRepository.existsByName(chatDto.getName());
        if (existsByName) throw new RuntimeException("Bunday chat mavjud");
        Set<User> users = userService.getUsersIn(chatDto.getUsersId());
        Chat chat = new Chat();
        chat.setName(chatDto.getName());
        chat.addUsers(users);
        return chatRepository.save(chat);
    }

    public Chat edit(Long id, ChatDto chatDto) {
        Optional<Chat> optionalChat = chatRepository.findById(id);
        if (optionalChat.isEmpty()) return new Chat();
        Chat chat = optionalChat.get();
        Set<User> users = userService.getUsersIn(chatDto.getUsersId());
        chat.setName(chatDto.getName());
        chat.addUsers(users);
        return chatRepository.save(chat);
    }

    @Transactional
    public String delete(Long id) {
        Optional<Chat> optionalChat = chatRepository.findById(id);
        if (optionalChat.isEmpty()) return "Not Found";
        Chat chat = optionalChat.get();
        Set<User> users = chat.getUsers();
        chat.removeUsers(users);
        chatRepository.deleteById(id);
        return "Deleted";
    }

}
