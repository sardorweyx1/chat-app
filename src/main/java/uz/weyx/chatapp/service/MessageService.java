package uz.weyx.chatapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.weyx.chatapp.entity.Chat;
import uz.weyx.chatapp.entity.Message;
import uz.weyx.chatapp.entity.MessageType;
import uz.weyx.chatapp.entity.User;
import uz.weyx.chatapp.repository.MessageRepository;
import uz.weyx.chatapp.service.minio.FileUploader;
import uz.weyx.chatapp.service.payload.UrlDto;
import uz.weyx.chatapp.service.payload.MessageDto;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final FileUploader fileUploader;

    public List<Message> getAllByUserId(Long id) {
        return messageRepository.findAllByUser_Id(id);
    }

    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    public String savePhoto(MessageDto messageDto) {
        MultipartFile file = messageDto.getFile();
        if (file != null) {
            boolean exists = userService.existsById(messageDto.getUserId());
            if (!exists) return "User Not Found";
            User user = userService.getById(messageDto.getUserId());
            Set<Chat> chats = userService.getChatsByUserId(messageDto.getUserId());
            if (chats.isEmpty()) return "Chat Not Found";
            Chat chat = chatService.getById(messageDto.getChatId());
            boolean contains = chats.contains(chat);
            if (!contains) return "You Can't Send Message To This Chat";
            Message message = new Message();
            String objectName = UUID.randomUUID().toString();
            message.setUser(user);
            message.setChat(chat);
            message.setContentType(MessageType.IMAGE);
            message.setObjectName(objectName);
            fileUploader.upload(objectName, file);
            messageRepository.save(message);
            return "Saved!";
        }
        return "Not Saved!";
    }

    public String saveText(MessageDto messageDto) {
        String content = messageDto.getContent();
        if (content.isEmpty()) return "Is Empty";
        boolean exists = userService.existsById(messageDto.getUserId());
        if (!exists) return "User Not Found";
        User user = userService.getById(messageDto.getUserId());
        Set<Chat> chats = userService.getChatsByUserId(messageDto.getUserId());
        if (chats.isEmpty()) return "Chat Not Found";
        Chat chat = chatService.getById(messageDto.getChatId());
        boolean contains = chats.contains(chat);
        if (!contains) return "You Can't Send Message To This Chat";
        Message message = new Message();
        message.setContentType(MessageType.TEXT);
        message.setUser(user);
        message.setChat(chat);
        message.setMessageText(messageDto.getContent());
        messageRepository.save(message);
        return "Saved Successfuly";
    }

    public String detele(Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            messageRepository.deleteById(id);
            return "Deleted";
        }
        return "Not Found";
    }

    public String edit(Long id, MessageDto messageDto) throws IOException {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isEmpty()) return "Not Found";
        Message oldMessage = optionalMessage.get();
        MessageType contentType = oldMessage.getContentType();
        if (contentType.name().equals("IMAGE")) return "Wrong type.Cannot edit";
        oldMessage.setMessageText(messageDto.getContent());
        messageRepository.save(oldMessage);
        return "Edited";
    }

    public List<UrlDto> getByChatId(Long id) {
        List<Message> messageList = messageRepository.findAllByChatId(id);
        List<UrlDto> messages = new ArrayList<>();
        UrlDto urlDto;
        for (Message message : messageList) {
            urlDto = new UrlDto();
            urlDto.setChatId(message.getChat().getId());
            urlDto.setUserId(message.getUser().getId());
            MessageType contentType = message.getContentType();
            if (contentType.equals(MessageType.TEXT)) {
                urlDto.setMessage(message.getMessageText());
            } else if (contentType.equals(MessageType.IMAGE)) {
                String tempUrl = fileUploader.getTempUrl(message.getObjectName());
                urlDto.setMessage(tempUrl);
            }
            messages.add(urlDto);
        }
        return messages;
    }
}

