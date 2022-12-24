package uz.weyx.chatapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    public Chat chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    public User user;

    @Enumerated(EnumType.STRING)
    public MessageType contentType;

    //if message is photo
    private String objectName;

    private String messageText;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    public Timestamp createdAt;
}
