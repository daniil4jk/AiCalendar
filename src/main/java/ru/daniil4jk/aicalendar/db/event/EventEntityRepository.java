package ru.daniil4jk.aicalendar.db.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.daniil4jk.aicalendar.db.user.UserEntity;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventEntityRepository extends JpaRepository<EventEntity, Long>,
        JpaSpecificationExecutor<EventEntity> {
    List<EventEntity> findAllByUserAndStartAfterAndEndBefore(UserEntity user, Instant start, Instant end);
}