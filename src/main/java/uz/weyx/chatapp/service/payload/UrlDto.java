package uz.weyx.chatapp.service.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlDto {
    private Long chatId;

    private Long userId;

    private String message;
}
