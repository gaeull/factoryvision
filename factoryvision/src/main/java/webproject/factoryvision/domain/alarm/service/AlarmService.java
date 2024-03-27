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

import java.util.List;

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

//    public void savedAlarmInfo(savedAlarmDto request) {
//        String userId = request.getUserId();
//        User user = userRepository.findByUserId(userId);
//
//        if (user != null) {
//            Alarm alarm = new Alarm();
//            alarm.setUser(user);
//            alarm.setUserInfo(userId);
//            alarm.setName(request.getName());
//            alarm.setPhone(request.getPhone());
//
//            alarmRepository.save(alarm);
//        } else {
//            throw new RuntimeException("유저 아이디가 존재하지 않습니다.");
//        }
//
//
//    }
}
