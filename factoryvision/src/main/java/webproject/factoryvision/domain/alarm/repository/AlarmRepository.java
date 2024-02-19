package webproject.factoryvision.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webproject.factoryvision.domain.alarm.entity.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
