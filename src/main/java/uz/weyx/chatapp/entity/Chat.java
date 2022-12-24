package uz.weyx.chatapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;


    @Column(unique = true)
    @CreationTimestamp
    private Timestamp createdAt;

    @ManyToMany(mappedBy = "chats",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public void addUsers(Set<User> users) {
        this.users.addAll(users);
        users.forEach(user -> user.getChats().add(this));
    }
    public void removeUsers(Set<User> users){
        users.forEach(user -> user.getChats().remove(this));
        this.users.removeAll(users);
    }

    public void addUser(User user) {
        this.users.add(user);
        user.getChats().add(this);
    }

    public void remove (User user){
        this.users.remove(user);
        user.getChats().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Chat chat = (Chat) o;
        return id != null && Objects.equals(id, chat.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
