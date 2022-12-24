package uz.weyx.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.weyx.chatapp.entity.Chat;
import uz.weyx.chatapp.entity.User;

import java.util.Optional;
import java.util.Set;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String name);

    boolean existsById(Long id);

    @Query("select u from Users u where u.Id in ?1")
    Set<User> findAllByIdIn(Set<Long> id);

}
