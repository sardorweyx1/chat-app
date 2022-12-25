package uz.weyx.chatapp.entity;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public enum Ext {
    PNG(MediaType.IMAGE_PNG_VALUE),
    JPEG(MediaType.IMAGE_JPEG_VALUE);
    private final String ext;

    Ext(String ext){
        this.ext=ext;
    }

    public static String getValue(String ext) {
        return Ext.valueOf(ext.toUpperCase()).getExt();
    }
}

