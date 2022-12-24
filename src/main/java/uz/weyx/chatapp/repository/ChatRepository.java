package uz.weyx.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.weyx.chatapp.entity.Chat;
import uz.weyx.chatapp.entity.User;

import java.util.Set;


@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
    boolean existsByName(String name);

    @Query("select c from Chat c inner join c.users users where users.Id = ?1")
    Set<User> findAllByUsersId(Set<Long> id);
}
