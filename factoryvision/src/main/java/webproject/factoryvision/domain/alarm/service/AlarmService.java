package webproject.factoryvision.domain.alarm.service;

import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import webproject.factoryvision.domain.alarm.dto.AlarmDto;
import webproject.factoryvision.domain.alarm.dto.savedAlarmDto;
import webproject.factoryvision.domain.alarm.entity.Alarm;
import webproject.factoryvision.domain.alarm.mapper.AlarmMapper;
import webproject.factoryvision.domain.alarm.repository.AlarmRepository;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Builder
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final AlarmMapper alarmMapper;
    private final UserRepository userRepository;

    public List<AlarmDto> getAllAlarms() {
        List<Alarm> alarmList = alarmRepository.findAll();
        return alarmMapper.toAlarmDtoList(alarmList);
    }

    public void savedAlarmInfo(savedAlarmDto request) {
        Long userId = request.getUserId();
        Optional<User> userCallAlarm = userRepository.findById(userId);

        if (userCallAlarm.isPresent()) {
            Alarm alarm = new Alarm();
            User user = userCallAlarm.get();

            alarm.setUserId(user.getUserId());
            alarm.setName(user.getName());
            alarm.setPhone(user.getPhone());
            alarm.setCreatedAt(LocalDateTime.now());

            alarmRepository.save(alarm);
        } else {
            throw new RuntimeException("유저 아이디가 존재하지 않습니다.");
        }


    }
}
