package uz.weyx.chatapp.service;

import com.google.common.io.ByteSource;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import uz.weyx.chatapp.entity.Chat;
import uz.weyx.chatapp.entity.Message;
import uz.weyx.chatapp.entity.MessageType;
import uz.weyx.chatapp.entity.User;
import uz.weyx.chatapp.repository.MessageRepository;
import uz.weyx.chatapp.service.minio.FileUploader;
import uz.weyx.chatapp.service.payload.UrlDto;
import uz.weyx.chatapp.service.payload.MessageDto;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final FileUploader fileUploader;

    public List<Message> getAllByUserId(Long id) {
        return messageRepository.findAllByUser_Id(id);
    }

    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    public String save(MessageDto messageDto) throws IOException {
        User user = userService.getById(messageDto.getUserId());
        Set<Chat> chats = user.getChats();
        Message message = new Message();
        message.setUser(user);
        message.setChat(searchFromSet(chats, messageDto.getChatId()));
        if (!Objects.isNull(messageDto.getExt())) {
            String objectName = UUID.randomUUID().toString();
            message.setContentType(MessageType.IMAGE);
            message.setContent(objectName + "." + messageDto.getExt());
            byte[] decode = Base64.getDecoder().decode(messageDto.getContent());
            InputStream img = ByteSource.wrap(decode).openStream();
            fileUploader.upload(objectName, img, messageDto);
        } else {
            message.setContentType(MessageType.TEXT);
            message.setContent(messageDto.getContent());
        }
        messageRepository.save(message);
        return "Saved!";
    }


    public String detele(Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            messageRepository.deleteById(id);
            return "Deleted";
        }
        return "Not Found";
    }

    public String edit(Long id, MessageDto messageDto) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isEmpty()) return "Not Found";
        Message oldMessage = optionalMessage.get();
        if (oldMessage.getContentType().name().equals("IMAGE")) return "Wrong type.Cannot edit";
        oldMessage.setContent(messageDto.getContent());
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
                urlDto.setMessage(message.getContent());
            } else if (contentType.equals(MessageType.IMAGE)) {
                String tempUrl = fileUploader.getTempUrl(message.getContent());
                urlDto.setMessage(tempUrl);
            }
            messages.add(urlDto);
        }
        return messages;
    }

    private Chat searchFromSet(Set<Chat> chats, Long chatId) {
        for (Chat chat : chats) {
            if (chat.getId().equals(chatId)) {
                return chat;
            }
        }
        throw new IllegalArgumentException("Chat Not Found");
    }
}

