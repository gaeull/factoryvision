package webproject.factoryvision.domain.admin.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webproject.factoryvision.domain.alarm.dto.AlarmDto;
import webproject.factoryvision.domain.alarm.entity.Alarm;
import webproject.factoryvision.domain.alarm.mapper.AlarmMapper;
import webproject.factoryvision.domain.alarm.repository.AlarmRepository;
import webproject.factoryvision.domain.user.dto.UserDto;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.mapper.UserMapper;
import webproject.factoryvision.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Builder
public class AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AlarmRepository alarmRepository;
    private final AlarmMapper alarmMapper;


    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.toUserDtolist(userList);
    }

    public List<AlarmDto> getAllAlarms() {
        return alarmRepository.findAll().stream()
                .map(this::mapToAlarmDto)
                .collect(Collectors.toList());
    }

    private AlarmDto mapToAlarmDto(Alarm alarm) {
        AlarmDto alarmDto = alarmMapper.toAlarmDto(alarm);
        alarmDto.setCreatedAt(alarm.getCreatedAt());
        return alarmDto;
    }

}
