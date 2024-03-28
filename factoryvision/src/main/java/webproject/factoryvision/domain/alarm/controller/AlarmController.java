package webproject.factoryvision.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.alarm.dto.AlarmDto;
import webproject.factoryvision.domain.alarm.dto.savedAlarmDto;
import webproject.factoryvision.domain.alarm.service.AlarmService;

import java.util.List;

@Tag(name = "비상호출 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/factoryvision/emergency")
public class AlarmController {

    private final AlarmService alarmService;
    private final HttpHeaders getHeaders;

//     알람 클릭 시, 사용자 정보 저장
    @PostMapping()
    @Operation(summary = "알람 클릭 시, 사용자 정보 저장", description = "userid(인덱스)만 request body로 넘겨주기")
    public ResponseEntity<?> savedAlarmInfo(@RequestBody savedAlarmDto request) {
        try {
            alarmService.savedAlarmInfo(request);
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(getHeaders)
                    .body("비상 버튼 클릭 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(getHeaders)
                    .body("유저 아이디가 존재하지 않습니다.");
        }
    }
}
