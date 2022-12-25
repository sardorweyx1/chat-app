package uz.weyx.chatapp.service.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageDto {

    @NotNull
    private Long chatId;

    @NotNull
    private Long userId;

    private String type;

    private String content;

    private String ext;

}
