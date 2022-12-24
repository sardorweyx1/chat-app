package uz.weyx.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.weyx.chatapp.entity.Chat;
import uz.weyx.chatapp.entity.Message;
import uz.weyx.chatapp.entity.User;


import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    @Query(value = "select m from Message m where m.user.Id = ?1")
    List<Message> findAllByUser_Id(Long id);


    @Query("select m from Message m where m.chat.id = ?1")
    List<Message> findAllByChatId(Long id);
}
