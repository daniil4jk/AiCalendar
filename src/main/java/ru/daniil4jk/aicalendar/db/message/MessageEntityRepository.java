package ru.daniil4jk.aicalendar.db.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageEntityRepository extends JpaRepository<MessageEntity, Long>,
        JpaSpecificationExecutor<MessageEntity> {

    List<MessageEntity> findByChat_IdAndChat_User(Long id, UserEntity user);

    Optional<MessageEntity> findByIdAndChat_User(Long id, UserEntity user);

    Optional<MessageEntity> findFirstByChat_IdAndChat_UserOrderByIdDesc(Long id, UserEntity user);
}