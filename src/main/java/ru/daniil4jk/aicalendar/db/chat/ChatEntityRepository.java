package ru.daniil4jk.aicalendar.db.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.util.List;

@Repository
public interface ChatEntityRepository extends JpaRepository<ChatEntity, Long>,
        JpaSpecificationExecutor<ChatEntity> {
    List<ChatEntity> getAllByUser(UserEntity user);
}