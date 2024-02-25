package webproject.factoryvision.domain.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.alarm.dto.AlarmDto;
import webproject.factoryvision.domain.alarm.dto.savedAlarmDto;
import webproject.factoryvision.domain.alarm.service.AlarmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/factoryvision/emergency")
public class AlarmController {

    private final AlarmService alarmService;

    // 호출 알림 조회
    @GetMapping()
//    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AlarmDto> getAllAlarms() {
        return alarmService.getAllAlarms();
    }

    // 알람 클릭 시, 사용자 정보 저장
    @PostMapping()
    public ResponseEntity<Void> savedAlarmInfo(@RequestBody savedAlarmDto request) {
        alarmService.savedAlarmInfo(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
